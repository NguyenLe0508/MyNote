package com.tuplv.mynote.fragment;

import static android.content.Context.MODE_PRIVATE;

import static com.tuplv.mynote.activity.MainActivity.mediaPlayer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.hmsscankit.WriterException;
import com.huawei.hms.ml.scan.HmsBuildBitmapOption;
import com.huawei.hms.ml.scan.HmsScan;
import com.tuplv.mynote.R;
import com.tuplv.mynote.activity.AddUpdateNoteActivity;
import com.tuplv.mynote.activity.ListCategoryActivity;
import com.tuplv.mynote.activity.MainActivity;
import com.tuplv.mynote.adapter.CategoryAdapter;
import com.tuplv.mynote.adapter.NoteAdapter;
import com.tuplv.mynote.database.MyDatabase;
import com.tuplv.mynote.interf.OnCategoryClickListener;
import com.tuplv.mynote.interf.OnNoteClickListener;
import com.tuplv.mynote.model.Category;
import com.tuplv.mynote.model.Note;

import java.util.List;

public class NoteFragment extends Fragment implements View.OnClickListener, OnNoteClickListener {

    List<Category> listCategory;
    CategoryAdapter categoryAdapter;

    List<Note> listNote;
    NoteAdapter noteAdapter;

    MyDatabase myDatabase;

    ImageView imgAddCategory;
    FloatingActionButton fabAddNote,fabMusic;
    RecyclerView rvCategory, rvNote;

    SharedPreferences sharedPreferences;
    int ROW = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        mapping(view);

        myDatabase = MyDatabase.getInstance(getActivity());

        imgAddCategory.setOnClickListener(this);
        fabAddNote.setOnClickListener(this);

        sharedPreferences = getActivity().getSharedPreferences("option", MODE_PRIVATE);
        ROW = sharedPreferences.getInt("ROW", 1);


            fabMusic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                    }else {
                        mediaPlayer.start();
                    }
                }
            });
        return view;

    }

    private void mapping(View view) {
        imgAddCategory = view.findViewById(R.id.imgAddCategory);
        fabAddNote = view.findViewById(R.id.fabAddNote);
        rvCategory = view.findViewById(R.id.lvCategory);
        rvNote = view.findViewById(R.id.rvNote);
        fabMusic = view.findViewById(R.id.fabMusic);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgAddCategory:
                startActivity(new Intent(getActivity(), ListCategoryActivity.class));
                break;
            case R.id.fabAddNote:
                startActivity(new Intent(getActivity(), AddUpdateNoteActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showDataCategory();
        showDataNote();
    }

    private void showDataNote() {
        if (MainActivity.SORT == 1) {
            listNote = myDatabase.sortDate("DESC");
        } else if (MainActivity.SORT == 2) {
            listNote = myDatabase.sortDate("ASC");
        } else if(MainActivity.SORT == 3){
            listNote = myDatabase.sortAZ("ASC");

        }else if (MainActivity.SORT == 4){
            listNote = myDatabase.sortAZ("DESC");
        }
        else {
            listNote = myDatabase.getAllNote();
        }
        setDataNoteToView();
    }

    //h??m ????? dmuc ra fragment
    private void showDataCategory() {
        listCategory = myDatabase.getAllCategory();
        categoryAdapter = new CategoryAdapter(getActivity(), R.layout.item_category, listCategory, this);
        rvCategory.setAdapter(categoryAdapter);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        rvCategory.setLayoutManager(staggeredGridLayoutManager);
    }

    public void setDataNoteToView() {
        StaggeredGridLayoutManager staggeredGridLayoutManager;
        noteAdapter = new NoteAdapter(getActivity(), R.layout.item_note, listNote, this);
        rvNote.setAdapter(noteAdapter);
//        if (sharedPreferences != null) {
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(ROW, StaggeredGridLayoutManager.VERTICAL);
//        } else {
//            staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
//        }
        rvNote.setLayoutManager(staggeredGridLayoutManager);
    }

    @Override
    public void goToActivityUpdate(Note note) {
        Intent intent = new Intent(getActivity(), AddUpdateNoteActivity.class);
        intent.putExtra("note", note);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("C???nh b??o!");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setMessage("X??a ghi ch?? n??y?");
        builder.setPositiveButton("X??a", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (myDatabase.deleteNote(note)) {
                    Toast.makeText(getActivity(), "X??a ghi ch?? th??nh c??ng", Toast.LENGTH_SHORT).show();
                    listNote.remove(note);
                    noteAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "X??a ghi ch?? th???t b???i", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("H???y", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    // L??y danh s??ch ghi ch?? b???ng id danh m???c
    @Override
    public void onGetListNoteByCategoryId(int id) {
        if (id == 1) {
            listNote = myDatabase.getAllNote();
        } else {
            listNote = myDatabase.getNoteByCategoryId(id);
        }
        setDataNoteToView();
    }

    @Override
    public void onShareNote(Note note) {
        int type = HmsScan.QRCODE_SCAN_TYPE;
        int width = 400;
        int height = 400;

        HmsBuildBitmapOption options =
                new HmsBuildBitmapOption.Creator().setBitmapBackgroundColor(Color.BLACK).setBitmapColor(android.R.color.white).setBitmapMargin(2).create();

        Bitmap qrBitmap = null;
        try {
            qrBitmap = ScanUtil.buildBitmap(note.noteMapper(), type, width, height, options);
        } catch (WriterException e) {
            Log.w("buildBitmap", e);
        }

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.activity_qr_code);

        ImageView imgQRCode = dialog.findViewById(R.id.imgQRCode);

        imgQRCode.setImageBitmap(qrBitmap);

        dialog.show();
    }
}
