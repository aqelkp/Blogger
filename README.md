# Blogger
An android app to view blogs


Following is not related to the project by any mean 

# Adding view

    overlay = (RelativeLayout) inflater.inflate(R.layout.overlay, null);
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    if(overlay!=null)
    {
        wm.removeView(overlay);
        overlay = null;
    }

# Working example of SMS reciever
http://www.androidhive.info/2015/08/android-adding-sms-verification-like-whatsapp-part-2/

