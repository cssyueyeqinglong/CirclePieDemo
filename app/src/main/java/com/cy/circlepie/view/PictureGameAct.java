package com.cy.circlepie.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.cy.circlepie.PicItemBean;
import com.cy.circlepie.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Administrator on 2017/7/13.
 * 小拼图游戏
 */

public class PictureGameAct extends AppCompatActivity {
    private static final String IMAGE_TYPE = "image/*";
    private static final int IMAGE_PIC_CODE = 0X12;
    private static final int IMAGE_TACK_PICK_CODE = 0X14;
    private static final int PERMISSIONCODE = 0X16;
    private int TYPE = 3;
    private final String TAG = this.getClass().getSimpleName();
    private boolean isTackPhoto;
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/img.png";
    private Bitmap lastBimtap;
    @BindView(R.id.gv)
    GridView gv;
    private BitmapAdapter adapter;
    private PicItemBean mBlackItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pic_game);
        ButterKnife.bind(this);

    }

    //打开相册选择图片
    public void choosePic(View view) {
        isTackPhoto = false;
        if (checkNeededPermission()) {
            selPic();
        }
    }

    //拍照
    public void tackPic(View view) {
        isTackPhoto = true;
        if (checkNeededPermission()) {
            selPic();
        }
    }

    public void selPic() {
        if (isTackPhoto) {//拍照
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = Uri.fromFile(new File(path));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, IMAGE_TACK_PICK_CODE);
        } else {//相册
            Intent intent = new Intent(Intent.ACTION_PICK, null);//Intent.ACTION_PICK相册的action
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_TYPE);
            startActivityForResult(intent, IMAGE_PIC_CODE);
        }
    }

    /**
     * 检查权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkNeededPermission() {
        if (checkSelfPermission(
                READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(
                        WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONCODE);
            return false;
        } else return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PIC_CODE) {//相册
                if (data != null && data.getData() != null) {
                    String filePath;
                    Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        cursor.close();
                    } else {
                        filePath = data.getData().getPath();
                    }
                    getBitmapList(filePath);
                    generateMapList();
                    setBitmapData();
                }
            } else if (requestCode == IMAGE_TACK_PICK_CODE) {//拍照
                getBitmapList(path);
                generateMapList();
                setBitmapData();
            }
        }
    }

    /**
     * 初始化图片资源，有序等分图片
     *
     * @param filePath
     */
    private void getBitmapList(String filePath) {
        datas.clear();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);//获取源文件
        int itemWidth = bitmap.getWidth() / TYPE;
        int itemHeight = bitmap.getHeight() / TYPE;
        for (int i = 0; i < TYPE; i++) {
            for (int j = 0; j < TYPE; j++) {
                PicItemBean item = new PicItemBean();
                item.bitmap = Bitmap.createBitmap(bitmap, itemWidth * j, itemHeight * i, itemWidth, itemHeight);//逐步剪裁图片
                item.bitmapId = i * TYPE + j + 1;
                item.itemId = i * TYPE + j + 1;
                datas.add(item);
            }
        }
        lastBimtap = datas.get(TYPE * TYPE - 1).bitmap;
        datas.remove(TYPE * TYPE - 1);//移除最后一张图片，然后添加一张空白图片
        PicItemBean item = new PicItemBean();
        item.bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.back);
        item.itemId = TYPE * TYPE;
        item.bitmapId = 0;
        datas.add(item);
        mBlackItem = item;//先将最后一个item赋值给空白的item
    }

    /**
     * 将图片进行混合成无序的
     */
    private void generateMapList() {
        for (int i = 0; i < datas.size(); i++) {
            int pos = (int) (Math.random() * TYPE * TYPE);
            swipeData(mBlackItem, datas.get(pos));
        }
        List<Integer> idDatas = new ArrayList<Integer>();
        for (int i = 0; i < datas.size(); i++) {
            idDatas.add(datas.get(i).bitmapId);
        }
        //判断生成是否有解
        if (canSolve(idDatas)) {
            return;
        } else {
            generateMapList();
        }
    }

    /**
     * 判断是否有解
     *
     * @param data 拼图id数组数据
     * @return
     */
    public boolean canSolve(List<Integer> data) {
        //获取空格id
        int blackId = mBlackItem.itemId;
        if (data.size() % 2 == 1) {
            return getInversions(data) % 2 == 0;
        } else {
            //从下往上数，空格位于奇数行，
            if (((blackId - 1) / TYPE) % 2 == 1) {
                return getInversions(data) % 2 == 0;
            } else {
                //从下往上数，空格位于偶数行，
                return getInversions(data) % 2 == 1;
            }
        }
    }

    /**
     * 倒置和算法
     *
     * @param data
     * @return 该序列的倒置和
     */
    public static int getInversions(List<Integer> data) {
        int inversions = 0;
        int inversionCount = 0;
        for (int i = 0; i < data.size(); i++) {
            for (int j = i + 1; j < data.size(); j++) {
                int index = data.get(i);
                if (data.get(j) != 0 && data.get(j) < index) {
                    inversionCount++;
                }
            }
            inversions += inversionCount;
            inversionCount = 0;
        }
        return inversions;
    }

    /**
     * 交换空白格数据和点击条目的数据
     *
     * @param blackItem
     * @param item
     */
    private void swipeData(PicItemBean blackItem, PicItemBean item) {
        int bitmapId = item.bitmapId;
        Bitmap bitmap = item.bitmap;
        item.bitmapId = blackItem.bitmapId;
        item.bitmap = blackItem.bitmap;
        blackItem.bitmap = bitmap;
        blackItem.bitmapId = bitmapId;
        mBlackItem = item;//注意，这里需要将空白格的值重新赋值，值为新的需要交换数据的item
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONCODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                selPic();
            } else {
                Toast.makeText(PictureGameAct.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    List<PicItemBean> datas = new ArrayList<>();

    /**
     * 显示数据
     */
    public void setBitmapData() {
        adapter = new BitmapAdapter(this);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PicItemBean bean = datas.get(position);
                //判断四周是否有空格
                if (couldChange(bean)) {
                    swipeData(mBlackItem, bean);
                    if (isSucceed()) {//拼图成功，将缺省的图片还原
                        mBlackItem.bitmap = lastBimtap;
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 点击条目是否可以和空白格交换位置数据
     * 依据是当处于同一行的时候，他们的itemId相差1就说明两者可以交换位置，如果是不同行，只有相差TYPE时
     * 才可以交换位置
     *
     * @param bean
     * @return
     */
    private boolean couldChange(PicItemBean bean) {
        if (Math.abs(bean.itemId - mBlackItem.itemId) == TYPE) {
            return true;//不同行的时候两个相差3就可以交换
        } else if (Math.abs(bean.itemId - mBlackItem.itemId) == 1) {//同行相差1
            return true;
        }
        return false;
    }

    /**
     * 拼图是否完成
     *
     * @return
     */
    private boolean isSucceed() {
        boolean result = true;
        for (int i = 0; i < datas.size(); i++) {
            PicItemBean item = datas.get(i);
            if (i < TYPE * TYPE - 1) {
                if (item.bitmapId != item.itemId) {
                    result = false;
                    break;
                }
            } else {
                if (item.bitmapId == 0 && item.itemId == TYPE * TYPE) {
                    result = true;
                } else {
                    result = false;
                }
            }
        }
        return result;
    }

    private class BitmapAdapter extends BaseAdapter {

        public BitmapAdapter(Context context) {
        }

        @Override
        public int getCount() {
            Log.d(TAG, "SIZE====" + datas.size());
            return datas == null ? 0 : datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(PictureGameAct.this).inflate(R.layout.item_img, null);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            ViewGroup.LayoutParams layoutParams = holder.iv.getLayoutParams();
            layoutParams.height = getScreenWidth() / 3;
            holder.iv.setLayoutParams(layoutParams);
            holder.iv.setImageBitmap(datas.get(position).bitmap);
            return convertView;
        }
    }

    private static class ViewHolder {
        ImageView iv;
    }

    public int getScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
}
