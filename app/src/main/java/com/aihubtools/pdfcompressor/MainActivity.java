package com.aihubtools.pdfcompressor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.database.Cursor;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class MainActivity extends Activity {
    private static final int PICK_PDF_REQUEST = 1001;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(48, 48, 48, 48);
        root.setBackgroundColor(0xFF0F172A);

        TextView title = new TextView(this);
        title.setText("PDF压缩工具");
        title.setTextColor(0xFFFFFFFF);
        title.setTextSize(26);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        TextView desc = new TextView(this);
        desc.setText("选择一个PDF文件，先完成本地读取和测试框架。后续可继续接入真实压缩算法。");
        desc.setTextColor(0xFFCBD5E1);
        desc.setTextSize(15);
        desc.setGravity(Gravity.CENTER);
        desc.setPadding(0, 28, 0, 28);
        root.addView(desc);

        Button pickButton = new Button(this);
        pickButton.setText("选择PDF文件");
        root.addView(pickButton);

        statusText = new TextView(this);
        statusText.setText("未选择文件");
        statusText.setTextColor(0xFF94A3B8);
        statusText.setTextSize(14);
        statusText.setGravity(Gravity.CENTER);
        statusText.setPadding(0, 30, 0, 0);
        root.addView(statusText);

        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPdfPicker();
            }
        });

        setContentView(root);
    }

    private void openPdfPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                String name = getFileName(uri);
                long size = getFileSize(uri);
                statusText.setText("已选择：" + name + "\n文件大小：" + formatSize(size) + "\n\n当前版本已完成PDF选择框架。下一步接入压缩保存功能。");
                Toast.makeText(this, "PDF读取成功", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = "selected.pdf";
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            try {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex >= 0 && cursor.moveToFirst()) {
                    result = cursor.getString(nameIndex);
                }
            } finally {
                cursor.close();
            }
        }
        return result;
    }

    private long getFileSize(Uri uri) {
        long result = -1;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            try {
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (sizeIndex >= 0 && cursor.moveToFirst() && !cursor.isNull(sizeIndex)) {
                    result = cursor.getLong(sizeIndex);
                }
            } finally {
                cursor.close();
            }
        }
        if (result < 0) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                if (inputStream != null) {
                    result = inputStream.available();
                    inputStream.close();
                }
            } catch (Exception ignored) {
                result = -1;
            }
        }
        return result;
    }

    private String formatSize(long bytes) {
        if (bytes < 0) return "未知";
        if (bytes < 1024) return bytes + " B";
        double kb = bytes / 1024.0;
        if (kb < 1024) return String.format("%.1f KB", kb);
        double mb = kb / 1024.0;
        return String.format("%.2f MB", mb);
    }
}
