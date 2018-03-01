package com.example.seddi.nfc_database;
        import android.app.PendingIntent;
        import android.content.Intent;
        import android.content.IntentFilter;

        import android.nfc.NfcAdapter;
        import android.nfc.Tag;
        import android.nfc.tech.Ndef;
        import android.os.Environment;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.FileWriter;
        import java.io.IOException;
        import java.text.SimpleDateFormat;
        import java.util.Date;

public class MainActivity extends AppCompatActivity implements Listener{

    public static final String TAG = MainActivity.class.getSimpleName();

    TextView  mTvMessage;
    private NFC_Read mNfcReadFragment;
    DataBaseNFC db;

    private boolean isDialogDisplayed = false ;

    private NfcAdapter mNfcAdapter;
    ListView l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initNFC();
        db=new DataBaseNFC(this,null);
    }
    public void show(){

        String[] data=db.showItems().split("/");
        l=(ListView)findViewById(R.id.list_item);
        ArrayAdapter<String> adapter=new ArrayAdapter(this,R.layout.single_row,R.id.textView,data);
        l.setAdapter(adapter);
    }


    private void initNFC(){

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    public void onDialogDisplayed() {

        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {

        isDialogDisplayed = false;

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(mNfcAdapter!= null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);
        this.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter!= null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    public String getSerialNumber(Tag tag){
        String serial_nbr = new String();

        for(int i = 0; i < tag.getId().length; i++){
            String x = Integer.toHexString(((int) tag.getId()[i] & 0xff));
            if(x.length() == 1){
                x = '0' + x;
            }
            serial_nbr += x + ' ';
        }

        return serial_nbr;
    }

    @Override
    protected void onNewIntent(Intent intent) {

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        //Base de donées

       if (tag != null) {//si on a reussi a lire la carte:
           Ndef ndef = Ndef.get(tag);
            if(!db.showItemByNFC(getSerialNumber(tag).substring(0,(getSerialNumber(tag).length()-1))).equals("")){
                db.updatePresence(getSerialNumber(tag).substring(0,(getSerialNumber(tag).length()-1)));
                this.show();
                //afficher le nom et prenom de l'etudiant

               // mTvMessage.setText(db.showItemByNFC(getSerialNumber(tag).substring(0,(getSerialNumber(tag).length()-1))));
               // mTvMessage.setTextSize(30);
              //  mTvMessage.setText(db.showItems());

            }else{
                Intent i=new Intent(this,AjouterEtudiant.class);
                i.putExtra("Id",getSerialNumber(tag).substring(0,(getSerialNumber(tag).length()-1)));
                startActivity(i);
               // db.insertItem("01255420",123L,"ks,dsq","kjdcj");
            }
            if (isDialogDisplayed) {
                mNfcReadFragment = (NFC_Read) getFragmentManager().findFragmentByTag(NFC_Read.TAG);
                mNfcReadFragment.onNfcDetected(ndef);
            }
        }
    }
    public void writeFile(String emargement) throws FileNotFoundException {
        String state=Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            File Root=Environment.getExternalStorageDirectory();
            File Dir=new File(Root.getAbsolutePath()+"/Emargement");
            if(!Dir.exists()){
                Dir.mkdir();
            }
            File file=new File(Dir,"Emargement.txt");
            try {
                FileOutputStream stream=new FileOutputStream(file);
                stream.write(emargement.getBytes());
                stream.close();
                Log.e("file","Message enregistré !!");
                Toast.makeText(getApplicationContext(),"Message enregistré !!",Toast.LENGTH_LONG).show();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }else {
            Toast.makeText(getApplicationContext(),"External storage found",Toast.LENGTH_LONG).show();
        }

    }
    public void Reset(View view){
        db.update(0);
        show();
    }
    public void save(View view) throws FileNotFoundException {
        String content="Nom     Prenom      Num etudiant \n\n";

        String[] data=db.showItems().split("/");
        for(int i=0;i<data.length;i++){
            content+=(data[i]+"\n");
        }

        this.writeFile(content);

    }


}