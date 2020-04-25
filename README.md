  
    
  
# xGetter Library for Android!  
[![](https://jitpack.io/v/KhunHtetzNaing/XGetter.svg)](https://jitpack.io/#KhunHtetzNaing/XGetter)  
  

#NOTE
***I don't know why google photos can't stream in Video Player (eg. ExoPlayer)  
But, you can download and play in JWPlayer -_-***

ရုပ်ရှင်နဲ့ Video App ဖန်တီးချင်သူတွေအနေနဲ့  
အသုံးဝင်မယ့် Lib လေးတစ်ခုဖန်တီးပေးထားပါတယ်။  
Video တွေကို အကန့်အသတ်မရှိ Free တင်လို့ရပြီ  
ကြည့်တဲ့အချိန်မှာကြော်ငြာအရမ်းတက်တဲ့  
Openload, Streamango တို့လိုလင့်တွေကနေ  
ကြော်ငြာကြည့်စရာမလိုပဲ  
တိုက်ရိုက်ဒေါင်းဖို့လင့်ဆွဲထုတ်ပေးနိုင်တဲ့ကောင်လေးပါ။  
ရလာတဲ့လင့်ကို တိုက်ရိုက်ဒေါင်းမလား  
ကိုယ်ပိုင် Player နဲ့တိုက်ရိုက်ပြမလား  
အဆင်ပြေသလိုအသုံးချနိုင်ပါတယ်။  
လောလာဆယ်ပါဝင်တဲ့ Site တွေကတော့  
  
 - **1. Google Drive**  
 - **2. DailyMotion**  
 - **3. Google Photos**  
 - **4. Mp4Upload**  
 - **5. Facebook**  
 - **6. Mediafire**  
 - **7. Ok.Ru**  
 - **8. VK**  
 - **9. Twitter**  
 - **10. Youtube**  
 - **11. SolidFiles**  
 - **12. Vidoza**  
 - **13. UptoStream**  
 - **14. SendVid**  
 - **15. FanSubs**  
 - **16. Uptobox**  
 - **17. FEmbed**  
 - **18. FileRio**  
  
တို့ပဲဖြစ်ပါတယ်။  
ဒီဆိုဒ်တွေအားလုံးကနေ ကြော်ငြာကြည့်စရာမလိုပဲတိုက်ရိုက်လင့်ထုတ်ပေးမှာပါ။  
အခြားအခမဲ့ Video တင်လို့ရတဲ့ Site တွေကိုလည်းထပ်ဖြည့်ပေးသွားပါ့မယ် :)  
အဲ့တော့ Video Sharing App အတွက် Host ဝယ်စရာမလိုတော့ဘူးလေနော် ;)  
  
အသုံးပြုနည်း  
===========  
  
ပထမဦးဆုံး **build.gradle(project)** ထဲက  
  
 allprojects { repositories { ....... maven { url 'https://jitpack.io' } //ဒီကုဒ်လေးထည့်ပေးပါ။ } }  
ပြီးရင် **build.gradle(app)** ထဲက  
  
 dependencies { ............... implementation 'com.github.KhunHtetzNaing:xGetter:2.0.0' }  
**Android Studio** သမားတွေဆိုရင်တော့ **Sync Now** လုပ်ပေးပါ။  
**AIDE** သမားဆိုရင်တော့ **Save** ပြီး **Download** လုပ်ခိုင်းရင်လုပ်ပေးပါ။  
**မှတ်ချက်။ ။အင်တာနက်ဖွင့်ထားဖို့လိုပါမယ်**  
  
**AndroidManifest.xml ထဲမှာ  
  
 <application ..... android:usesCleartextTraffic="true">  
ထည့်ပေးဖို့လိုပါမယ်။  
  
ပြီးရင်တော့ကိုယ်ခေါ်ချင်တဲ့ Activity ကနေ  
  
 XGetter xGetter = new XGetter(this); xGetter.onFinish(new XGetter.OnTaskCompleted() {  
 @Override public void onTaskCompleted(ArrayList<XModel> vidURL, boolean multiple_quality) { if (multiple_quality){ //This video you can choose qualities for (XModel model : vidURL){ String url = model.getUrl(); //If google drive video you need to set cookie for play or download String cookie = model.getCookie(); } }else { //If single String url = vidURL.get(0).getUrl(); } }  
 @Override public void onError() { //Error } });  
 xGetter.find("Video လင့်");  
## IMPORTANT  
Okhttp3 ကိုအသုံးပြုထားသည့်အတွက်  
**proguard-rules.pro** ထည့်ပေးရန်လိုအပ်ပါသည်။  
  
 # JSR 305 annotations are for embedding nullability information.     -dontwarn javax.annotation.**  
     # A resource is loaded with a relative path so the package of this class must be preserved.       
      
    -keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase  
     # Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.  
     -dontwarn org.codehaus.mojo.animal_sniffer.*  
     # OkHttp platform used only on JVM and when Conscrypt dependency is available.  
     -dontwarn okhttp3.internal.platform.ConscryptPlatform  
  
  
အသုံးပြုရတာလည်းလွယ်ပါတယ် :)  
- Example APK => http://bit.ly/2TDvLQ2  
- Example Project => http://bit.ly/2Sr6Hiw  
- Repo => https://github.com/KhunHtetzNaing/xGetter  
#HtetzNaing #XGetter #2019