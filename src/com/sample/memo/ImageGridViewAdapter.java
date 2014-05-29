package com.sample.memo;
  
import android.content.Context; 
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.GridView;
import android.widget.BaseAdapter;  
import android.widget.ImageView;  

import java.util.List;  


public class ImageGridViewAdapter extends BaseAdapter {  
    private List<String> fileList = null;  
    private LayoutInflater inflater = null;  

    static class ViewHolder{  
        ImageView imageView;  
    }  
  
    public ImageGridViewAdapter(Context context, List<String> fileList) {  
        this.fileList = fileList;  
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
        ImageView iv = (ImageView)convertView;  
        ViewHolder holder;  
        if (iv == null) {  
            holder = new ViewHolder();  
            iv = (ImageView)(inflater.inflate(R.layout.thumbnail, null));
            holder.imageView = (ImageView)iv.findViewById(R.id.thumbnailView);
            iv.setLayoutParams(new GridView.LayoutParams(85, 85));
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setPadding(8, 8, 8, 8);

            String s = fileList.get(position);
            int len = (s != null ? s.length() : 0);
            if (len >= 4 && s.substring(len-4, len).equals("jpg")) {
                new ImageDownloadTask(holder.imageView, fileList.get(position)).execute();
                iv.setTag(holder);
            } else {
                holder.imageView.setImageResource(R.drawable.text);
                iv.setTag(holder);
            }
        } else {  
            holder = (ViewHolder)iv.getTag();  
        }  
        return iv;  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    public Object getItem(int position) {  
        return fileList.get(position);  
    }  
  
    @Override  
    public int getCount() {  
        return fileList.size();  
    }  
    
        public boolean remove(String uri) {
        return false;
    }
}  