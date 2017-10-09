package com.example.gaurang.pdfspeaker;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import android.support.v7.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import static android.R.attr.data;
import static android.content.Intent.ACTION_OPEN_DOCUMENT;
import static com.example.gaurang.pdfspeaker.R.id.pdfView;


public class MainActivity extends AppCompatActivity {

    private final int FILE_SELECT_CODE = 42;
    TextToSpeech tts;
    Button speak, stop, next, prev;
    String text;
    EditText et;
    PDFView pdfView;
PdfReader reader=null;
    public Uri uri = null;
    public File dir,myfile=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // et = (EditText) findViewById(R.id.editText1);

        speak=(Button) findViewById(R.id.speak);
        stop=(Button) findViewById(R.id.stop);
        next=(Button)findViewById(R.id.next_page);
        prev=(Button)findViewById(R.id.prev_page);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        File dir = Environment.getExternalStorageDirectory();
        Intent rcv = getIntent();
        int page = rcv.getIntExtra("page", 0);
        String path = rcv.getStringExtra("path_rtrn");

        pdfView.fromFile(myfile).defaultPage(page - 1).load();

        tts = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS) {
                    //int result =
                    tts.setLanguage(Locale.US);
                }
            }
        });

        if (page == 0) {
            myfile = new File(dir, "/sample_small.pdf");
        } else {
            myfile = new File(path);
        }

       // pdfView.fromAsset("R1.pdf").load();

        speak.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "speakingfirst..", Toast.LENGTH_SHORT).show();

                try {
                    reader = new PdfReader(String.valueOf(myfile));
                    int pn= pdfView.getCurrentPage();
                    Toast.makeText(MainActivity.this, "speakingfirst..", Toast.LENGTH_SHORT).show();

                    String text = PdfTextExtractor.getTextFromPage(reader, pn + 1).trim(); //Extracting the content from the different pages
                    Toast.makeText(MainActivity.this, "speaking..", Toast.LENGTH_SHORT).show();
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tts.isSpeaking()) {
                    Toast.makeText(MainActivity.this,"stoping tts", Toast.LENGTH_SHORT).show();
                    tts.stop();
                } else {
                    Toast.makeText(MainActivity.this, "not speaking at all!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.open:
                file_chooser();
                 return true;

            case R.id.bookmark:
                Toast.makeText(MainActivity.this, "Save is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.bookmarklist:
                Toast.makeText(MainActivity.this, "Search is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.jump:
                Toast.makeText(MainActivity.this, "Search is Selected", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

       Toast.makeText(this,"Uri: ",Toast.LENGTH_SHORT).show();
      /*  if ( resultCode == Activity.RESULT_OK && resultData!=null) {
                return;
        }
        uri = resultData.getData();
        pdfView.fromUri(uri).load();

        String path = uri.getLastPathSegment();

        String final_name = uri.getLastPathSegment();
        final_name = final_name.replace("primary:", "");
        final_name = "/" + final_name;
        dir = Environment.getExternalStorageDirectory();
        myfile = new File(dir, final_name);*/
        if (resultCode != Activity.RESULT_OK && resultData != null) {
            return;
        } else {
            uri = resultData.getData();

            pdfView.fromUri(uri).load();

            String path = uri.getLastPathSegment();

            String final_name = uri.getLastPathSegment();
            final_name = final_name.replace("primary:", "");
            final_name = "/" + final_name;
            File dir = Environment.getExternalStorageDirectory();
            myfile = new File(dir, final_name);
        }


        super.onActivityResult(requestCode, resultCode, resultData);

    }

  /*  private void ConvertTextToSpeech() {
        text = "hello";

        System.out.println("gaurang: " + text);
        if (text == null || "".equals(text)) {
            text = "Content not available";
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        } else
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }*/


    public void file_chooser(){
        Intent intent = new Intent(ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select a file"),FILE_SELECT_CODE);
    }


}







