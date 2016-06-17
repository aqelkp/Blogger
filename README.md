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
