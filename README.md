
# xGetter Library for Android!

[![](https://jitpack.io/v/KhunHtetzNaing/XGetter.svg)](https://jitpack.io/#KhunHtetzNaing/XGetter)

  
**#Zawgyi**

႐ုပ္ရွင္နဲ႔ Video App ဖန္တီးခ်င္သူေတြအေနနဲ႔

အသုံးဝင္မယ့္ Lib ေလးတစ္ခုဖန္တီးေပးထားပါတယ္။

Video ေတြကို အကန္႔အသတ္မရွိ Free တင္လို႔ရၿပီး

ၾကည့္တဲ့အခ်ိန္မွာေၾကာ္ျငာအရမ္းတက္တဲ့

Openload, Streamango တို႔လိုလင့္ေတြကေန

ေၾကာ္ျငာၾကည့္စရာမလိုပဲ

တိုက္႐ိုက္ေဒါင္းဖို႔လင့္ဆြဲထုတ္ေပးႏိုင္တဲ့ေကာင္ေလးပါ။

ရလာတဲ့လင့္ကို တိုက္႐ိုက္ေဒါင္းမလား

ကိုယ္ပိုင္ Player နဲ႔တိုက္႐ိုက္ျပမလား

အဆင္ေျပသလိုအသုံးခ်ႏိုင္ပါတယ္။

ေလာလာဆယ္ပါဝင္တဲ့ Site ေတြကေတာ့
- **Facebook**

- **Google Drive**

- **Google Photos**

-  **Openload**

-  **Streamango**

-  **MegaUp**

-  **Streamcherry**

-  **RapidVideo**

-  **Mp4Upload**

-  **VidCloud**

-  **SendVid**

တို႔ပဲျဖစ္ပါတယ္။

ဒီဆိုဒ္ေတြအားလုံးကေန ေၾကာ္ျငာၾကည့္စရာမလိုပဲတိုက္႐ိုက္လင့္ထုတ္ေပးမွာပါ။

အျခားအခမဲ့ Video တင္လို႔ရတဲ့ Site ေတြကိုလည္းထပ္ျဖည့္ေပးသြားပါ့မယ္ :)

အဲ့ေတာ့ Video Sharing App အတြက္ Host ဝယ္စရာမလိုေတာ့ဘူးေလေနာ္ ;)

Lib နာမည္ကိုေတာ့ **xGetter** လို႔ပဲေပးထားပါတယ္။

  

အသုံးျပဳနည္း

===========

ပထမဦးဆုံး **build.gradle(project)** ထဲက

  

    allprojects {
    
    repositories {
    
    .......
    
    maven { url 'https://jitpack.io' } //ဒီကုဒ္ေလးထည့္ေပးပါ။
    
    }
    
    }

  

ၿပီးရင္ **build.gradle(app)** ထဲက

  

    dependencies {
    
    .........
    
    implementation 'com.github.KhunHtetzNaing:xGetter:1.2'
    
    }

  

**Android Studio** သမားေတြဆိုရင္ေတာ့ **Sync Now** လုပ္ေပးပါ။

**​AIDE** သမားဆိုရင္ေတာ့ **Save** ၿပီး **Download** လုပ္ခိုင္းရင္လုပ္ေပးပါ။

**မွတ္ခ်က္။ ။အင္တာနက္ဖြင့္ထားဖို႔လိုပါမယ္**

  

ၿပီးရင္ေတာ့ကိုယ္ေခၚခ်င္တဲ့ Activity ကေန

  

    XGetter xGetter = new XGetter(this);
    
    xGetter.onFinish(new XGetter.OnTaskCompleted() {
    
    @Override
    
    public void onTaskCompleted(String vidURL) {
    
    // တိုက္႐ိုက္လင့္က vidURL
    
    }
    
    @Override  
    public void onFbTaskCompleted(String sd, String hd) {  
        //Facebook လင့္ျဖစ္ပါက ဒီထဲမွာ HD, SD ျပန္ရလာမည္
    }
    
    @Override
    
    public void onError() {
    
    // Error
    
    }
    
    });
    
    xGetter.find("Video လင့္");

  

အသုံးျပဳရတာလည္းလြယ္ပါတယ္ :)

  

- Example APK => http://bit.ly/2TDvLQ2

- Example Project => http://bit.ly/2Sr6Hiw

- Repo => https://github.com/KhunHtetzNaing/xGetter

  

#HtetzNaing #XGetter #2019

##
**#Unicode**
ရုပ်ရှင်နဲ့ Video App ဖန်တီးချင်သူတွေအနေနဲ့

အသုံးဝင်မယ့် Lib လေးတစ်ခုဖန်တီးပေးထားပါတယ်။

Video တွေကို အကန့်အသတ်မရှိ Free တင်လို့ရပြီး

ကြည့်တဲ့အချိန်မှာကြော်ငြာအရမ်းတက်တဲ့

Openload, Streamango တို့လိုလင့်တွေကနေ

ကြော်ငြာကြည့်စရာမလိုပဲ

တိုက်ရိုက်ဒေါင်းဖို့လင့်ဆွဲထုတ်ပေးနိုင်တဲ့ကောင်လေးပါ။

ရလာတဲ့လင့်ကို တိုက်ရိုက်ဒေါင်းမလား

ကိုယ်ပိုင် Player နဲ့တိုက်ရိုက်ပြမလား

အဆင်ပြေသလိုအသုံးချနိုင်ပါတယ်။

လောလာဆယ်ပါဝင်တဲ့ Site တွေကတော့
- **Facebook**

- **Google Drive**

- **Google Photos**

-  **Openload**

-  **Streamango**

-  **MegaUp**

-  **Streamcherry**

-  **RapidVideo**

-  **Mp4Upload**

-  **VidCloud**

-  **SendVid**

တို့ပဲဖြစ်ပါတယ်။

ဒီဆိုဒ်တွေအားလုံးကနေ ကြော်ငြာကြည့်စရာမလိုပဲတိုက်ရိုက်လင့်ထုတ်ပေးမှာပါ။

အခြားအခမဲ့ Video တင်လို့ရတဲ့ Site တွေကိုလည်းထပ်ဖြည့်ပေးသွားပါ့မယ် :)

အဲ့တော့ Video Sharing App အတွက် Host ဝယ်စရာမလိုတော့ဘူးလေနော် ;)

Lib နာမည်ကိုတော့ **xGetter** လို့ပဲပေးထားပါတယ်။

  

အသုံးပြုနည်း

===========

ပထမဦးဆုံး **build.gradle(project)** ထဲက

  

    allprojects {
    
    repositories {
    
    .......
    
    maven { url 'https://jitpack.io' } //ဒီကုဒ်လေးထည့်ပေးပါ။
    
    }
    
    }

  

ပြီးရင် **build.gradle(app)** ထဲက

  

    dependencies {
    
    .........
    
    implementation 'com.github.KhunHtetzNaing:xGetter:1.2'
    
    }

  

**Android Studio** သမားတွေဆိုရင်တော့ **Sync Now** လုပ်ပေးပါ။

**​AIDE** သမားဆိုရင်တော့ **Save** ပြီး **Download** လုပ်ခိုင်းရင်လုပ်ပေးပါ။

**မှတ်ချက်။ ။အင်တာနက်ဖွင့်ထားဖို့လိုပါမယ်**

  

ပြီးရင်တော့ကိုယ်ခေါ်ချင်တဲ့ Activity ကနေ

  

    XGetter xGetter = new XGetter(this);
    
    xGetter.onFinish(new XGetter.OnTaskCompleted() {
    
    @Override
    
    public void onTaskCompleted(String vidURL) {
    
    // တိုက်ရိုက်လင့်က vidURL
    
    }
    
    @Override  
    public void onFbTaskCompleted(String sd, String hd) {  
        //Facebook လင့်ဖြစ်ပါက ဒီထဲမှာ HD, SD ပြန်ရလာမည်
    }
    
    @Override
    
    public void onError() {
    
    // Error
    
    }
    
    });
    
    xGetter.find("Video လင့္");

  

အသုံးပြုရတာလည်းလွယ်ပါတယ် :)

  

- Example APK => http://bit.ly/2TDvLQ2

- Example Project => http://bit.ly/2Sr6Hiw

- Repo => https://github.com/KhunHtetzNaing/xGetter

  

#HtetzNaing #XGetter #2019
