package com.htetznaing.lowcostvideo.Utils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//Special thanks & credit => https://github.com/cylonu87/JsUnpacker

public class JSUnpacker {
    private String packedJS = null;

    /**
     * @param  packedJS javascript P.A.C.K.E.R. coded.
     *
     */
    public JSUnpacker(String packedJS) {
        this.packedJS = packedJS;
    }

    /**
     * Detects whether the javascript is P.A.C.K.E.R. coded.
     *
     * @return true if it's P.A.C.K.E.R. coded.
     *
     */
    public boolean detect() {
        if (packedJS!=null) {
            String js = packedJS.replace(" ", "");
            Pattern p = Pattern.compile("eval\\(function\\(p,a,c,k,e,(?:r|d)");
            Matcher m = p.matcher(js);
            return m.find();
        }return false;
    }

    /**
     * Unpack the javascript
     *
     * @return the javascript unpacked or null.
     *
     */
    public String unpack() {
        String js = new String(packedJS);
        try {
            Pattern p = Pattern.compile("\\}\\s*\\('(.*)',\\s*(.*?),\\s*(\\d+),\\s*'(.*?)'\\.split\\('\\|'\\)", Pattern.DOTALL);
            Matcher m = p.matcher(js);
            if(m.find() && m.groupCount() == 4) {
                String payload = m.group(1).replace("\\'", "'");
                String radixStr = m.group(2);
                String countStr = m.group(3);
                String[] symtab = m.group(4).split("\\|");

                int radix = 36;
                int count = 0;
                try {
                    radix = Integer.parseInt(radixStr);
                } catch(Exception e) {
                }
                try {
                    count = Integer.parseInt(countStr);
                } catch(Exception e) {
                }

                if(symtab.length != count) {
                    throw new Exception ("Unknown p.a.c.k.e.r. encoding");
                }

                Unbase unbase = new Unbase(radix);
                p = Pattern.compile("\\b\\w+\\b");
                m = p.matcher(payload);
                StringBuilder decoded = new StringBuilder(payload);
                int replaceOffset = 0;
                while(m.find()) {
                    String word = m.group(0);

                    int x = unbase.unbase(word);
                    String value = null;
                    if(x < symtab.length) {
                        value = symtab[x];
                    }

                    if(value != null && value.length() > 0) {
                        decoded.replace(m.start() + replaceOffset, m.end() + replaceOffset, value);
                        replaceOffset += (value.length() - word.length());
                    }
                }
                return decoded.toString();
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class Unbase {
        private final String ALPHABET_62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private final String ALPHABET_95 = " !\"#$%&\\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        private String alphabet = null;
        private HashMap<String, Integer> dictionnary = null;
        private int radix;

        Unbase(int radix) {
            this.radix = radix;

            if (radix > 36) {
                if (radix < 62) {
                    alphabet = ALPHABET_62.substring(0, radix);
                } else if (radix > 62 && radix < 95) {
                    alphabet = ALPHABET_95.substring(0, radix);
                } else if (radix == 62) {
                    alphabet = ALPHABET_62;
                } else if (radix == 95) {
                    alphabet = ALPHABET_95;
                }

                dictionnary = new HashMap<>(95);
                for (int i = 0; i < alphabet.length(); i++) {
                    dictionnary.put(alphabet.substring(i, i + 1), i);
                }
            }
        }

        int unbase(String str) {
            int ret = 0;

            if (alphabet == null) {
                ret = Integer.parseInt(str, radix);
            } else {
                String tmp = new StringBuilder(str).reverse().toString();
                for (int i = 0; i < tmp.length(); i++) {
                    ret += Math.pow(radix, i) * dictionnary.get(tmp.substring(i, i + 1));
                }
            }
            return ret;
        }
    }
}