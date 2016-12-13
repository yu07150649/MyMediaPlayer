package com.example.yu.mymediaplayer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Vector;

public class MyFileAcitivity extends Activity {
    private final String[]FILE_Matable={
            ".3gp",".mov",".avi",".rmvb",".wmv",".mp3",".mp4"
    };
    private Vector<String> items = null;
    private Vector<String> paths = null;
    private Vector<String> sizes = null;
    private String rootPath = "/mnt/sdcard";
    private EditText pathEditText;
    private Button queryButton;
    private ListView fileListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("多媒体文件浏览");
        setContentView(R.layout.activity_my_file_acitivity);
        pathEditText = (EditText) findViewById(R.id.path_edit);
        queryButton = (Button) findViewById(R.id.qry_button);
        fileListView = (ListView) findViewById(R.id.file_listview);
        queryButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(pathEditText.getText().toString());
                if(file.exists())
                {
                    if(file.isFile())
                    {
                        openFile(pathEditText.getText().toString());
                    }else{
                        getFileDir(pathEditText.getText().toString());
                    }
                }else{
                    Toast.makeText(MyFileAcitivity.this,"找不到该位置，请确定位置是否正确！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                fileOrDir(paths.get(i));
            }
        });
        getFileDir(rootPath);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            pathEditText = (EditText) findViewById(R.id.path_edit);
            File file= new File(pathEditText.getText().toString());
            if(rootPath.equals(pathEditText.getText().toString().trim()))
            {
                return super.onKeyDown(keyCode,event);
            }else{
                getFileDir(file.getParent());
                return true;
            }
        }else{
            return super.onKeyDown(keyCode, event);
         }
    }
    private void fileOrDir(String path){
        File file = new File(path);
        if(file.isDirectory())
        {
            getFileDir(file.getPath());
        }else{
            openFile(path);
        }
    }
    private void getFileDir(String filePath){
        pathEditText.setText(filePath);
        items = new Vector<String>();
        paths = new Vector<String>();
        sizes = new Vector<String>();
        File f = new File(filePath);
        File [] files = f.listFiles();
        if(files!=null)
        {
            for(int i =0;i<files.length;i++)
            {
                if(files[i].isDirectory())
                {
                    items.add(files[i].getName());
                    paths.add(files[i].getPath());
                    sizes.add("");
                }
            }
            for(int i=0;i<files.length;i++)
            {
                if(files[i].isFile())
                {
                    String fileName = files[i].getName();
                    int index = fileName.lastIndexOf(".");
                    if(index>0)
                    {
                        String endName = fileName.substring(index,fileName.length()).toLowerCase();
                        String type = null;
                        for(int x =0;x<FILE_Matable.length;x++)
                        {
                            if(endName.equals(FILE_Matable[x]))
                            {
                                type = FILE_Matable[x];
                                break;
                            }
                        }
                        if(type !=null)
                        {
                            items.add(files[i].getName());
                            paths.add(files[i].getPath());
                            sizes.add("");
                        }
                    }
                }
            }
        }
        fileListView.setAdapter(new FileListAdapter(this,items));
    }
    private void openFile(String path){
        Intent intent =new Intent(MyFileAcitivity.this,MainActivity.class);
        intent.putExtra("path",path);
        startActivity(intent);
        finish();
    }
    class FileListAdapter extends BaseAdapter
    {
        private Vector<String> items = null;
        private MyFileAcitivity myFile;
        public FileListAdapter(MyFileAcitivity myFile,Vector<String> items)
        {
            this.items = items;
            this.myFile = myFile;
        }
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.elementAt(i);
        }

        @Override
        public long getItemId(int i) {
            return items.size();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null)
            {
                view = myFile.getLayoutInflater().inflate(R.layout.file_item,null);
            }
            TextView name = (TextView) view.findViewById(R.id.name);
            ImageView music = (ImageView) view.findViewById(R.id.music);
            ImageView folder = (ImageView) view.findViewById(R.id.folder);
            name.setText(items.elementAt(i));
            if(sizes.elementAt(i).equals(""))
            {
                music.setVisibility(View.GONE);
                folder.setVisibility(View.VISIBLE);
            }else
            {
                folder.setVisibility(View.GONE);
                music.setVisibility(View.VISIBLE);
            }
            return view;
        }
    }
}
