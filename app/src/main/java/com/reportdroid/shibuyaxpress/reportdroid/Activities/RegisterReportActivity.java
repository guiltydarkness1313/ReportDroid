package com.reportdroid.shibuyaxpress.reportdroid.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.reportdroid.shibuyaxpress.reportdroid.Models.User;
import com.reportdroid.shibuyaxpress.reportdroid.R;
import com.reportdroid.shibuyaxpress.reportdroid.Models.ResponseMessage;
import com.reportdroid.shibuyaxpress.reportdroid.Services.ApiService;
import com.reportdroid.shibuyaxpress.reportdroid.Services.ApiServiceGenerator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterReportActivity extends AppCompatActivity {

    EditText inputTitle,inputDesc,inputAddress;
    Button btnAddImage,btnRegister,btnLocate;
    ImageView imgSample;
    private static final String TAG = RegisterReportActivity.class.getSimpleName();
    File rutaImgGallery;
    Uri imgGallery;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_report);

        inputAddress=findViewById(R.id.input_address);
        inputDesc=findViewById(R.id.input_des);
        inputTitle=findViewById(R.id.input_title);

        btnAddImage=findViewById(R.id.btn_img);
        btnRegister=findViewById(R.id.btn_register);
        btnLocate=findViewById(R.id.btn_locate_reg);
        imgSample=findViewById(R.id.img_new_add);

        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launcher=new Intent(RegisterReportActivity.this,MapsActivity.class);
                startActivity(launcher);
            }
        });
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final CharSequence[] items = {"Subir imagen desde galeria","tomar foto", "Salir"};

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setTitle("Opciones");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item){
                            case 0:
                                //subir imagen desde galeria
                                pickImage(v);
                                break;
                            case 1:
                                takePicture(v);
                                break;
                            case 2:
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
    }


    /**
     * Camera handler
     */

    private static final int CAPTURE_IMAGE_REQUEST = 300;
    public static final int PICK_IMAGE = 1;
    private Uri mediaFileUri;
    String selectedPath;
    public void takePicture(View view) {
        try {

            if (!permissionsGranted()) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_LIST, PERMISSIONS_REQUEST);
                return;
            }

            // Creando el directorio de imágenes (si no existe)
            File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    throw new Exception("Failed to create directory");
                }
            }

            // Definiendo la ruta destino de la captura (Uri)
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
            mediaFileUri = Uri.fromFile(mediaFile);

            // Iniciando la captura
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaFileUri);
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            Toast.makeText(this, "Error en captura: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void pickImage(View view){
        try {
            if (!permissionsGranted()) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_LIST, PERMISSIONS_REQUEST);
                return;
            }
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }catch (Exception e1){
            Toast.makeText(this,e1.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_REQUEST) {
            // Resultado en la captura de la foto
            if (resultCode == RESULT_OK) {
                try {
                    Log.d(TAG, "ResultCode: RESULT_OK");
                    // Toast.makeText(this, "Image saved to: " + mediaFileUri.getPath(), Toast.LENGTH_LONG).show();

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mediaFileUri);

                    // Reducir la imagen a 800px solo si lo supera
                    bitmap = scaleBitmapDown(bitmap, 800);

                    imgSample.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                    Toast.makeText(this, "Error al procesar imagen: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "ResultCode: RESULT_CANCELED");
            } else {
                Log.d(TAG, "ResultCode: " + resultCode);
            }
        }
        if (requestCode==PICK_IMAGE){

            if(resultCode==RESULT_OK){
                try {
                    imgGallery=data.getData();
                    Toast.makeText(this,imgGallery.toString(),Toast.LENGTH_SHORT).show();
                    InputStream stream=getContentResolver().openInputStream(data.getData());
                    bitmap= BitmapFactory.decodeStream(stream);
                    assert stream != null;
                    stream.close();
                    imgSample.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }





    public void callRegister(View view) {

        String titulo =inputTitle.getText().toString();
        String descripcion = inputDesc.getText().toString();
        String direccion = inputAddress.getText().toString();
        //obtener id del prro
        String id=String.valueOf(User.getInstance().getId());
                //
        if (titulo.isEmpty() || descripcion.isEmpty()||direccion.isEmpty()) {
            Toast.makeText(this, "faltan ingresar campos requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<ResponseMessage> call = null;

        if (mediaFileUri == null) {
            // Si no se incluye imagen hacemos un envío POST simple
            //call = service.createProducto(nombre, precio, detalles);
            // Reducir la imagen a 800px solo si lo supera
            if(imgGallery==null){
                call=service.createReport(titulo,direccion,descripcion,id);

            }else{
                // Si no se incluye imagen hacemos un envío POST simple
                //call = service.createProducto(nombre, precio, detalles);
                // Reducir la imagen a 800px solo si lo supera
                bitmap = scaleBitmapDown(bitmap, 800);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
                MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", imgGallery.getPath(), requestFile);
                RequestBody tituloPart = RequestBody.create(MultipartBody.FORM, titulo);
                RequestBody direccionPart = RequestBody.create(MultipartBody.FORM, direccion);
                RequestBody descripcionPart = RequestBody.create(MultipartBody.FORM, descripcion);
                RequestBody idPart = RequestBody.create(MultipartBody.FORM, id);
                call=service.createReportWithImage(tituloPart,direccionPart,descripcionPart,idPart,imagenPart);
            }

        } else {

            // Si se incluye hacemos envió en multiparts

            File file = new File(mediaFileUri.getPath());
            Log.d(TAG, "File: " + file.getPath() + " - exists: " + file.exists());

            // Podemos enviar la imagen con el tamaño original, pero lo mejor será comprimila antes de subir (byteArray)
            // RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);

            Bitmap bitmap = BitmapFactory.decodeFile(mediaFileUri.getPath());
            // Reducir la imagen a 800px solo si lo supera
            bitmap = scaleBitmapDown(bitmap, 800);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
            MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", file.getName(), requestFile);

            RequestBody tituloPart = RequestBody.create(MultipartBody.FORM, titulo);
            RequestBody direccionPart = RequestBody.create(MultipartBody.FORM, direccion);
            RequestBody descripcionPart = RequestBody.create(MultipartBody.FORM, descripcion);
            RequestBody idPart = RequestBody.create(MultipartBody.FORM, id);
            call=service.createReportWithImage(tituloPart,direccionPart,descripcionPart,idPart,imagenPart);

        }

        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        ResponseMessage responseMessage = response.body();
                        Log.d(TAG, "responseMessage: " + responseMessage);

                        Toast.makeText(RegisterReportActivity.this, responseMessage.getMessage(), Toast.LENGTH_LONG).show();
                        finish();

                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(RegisterReportActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Throwable x) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(RegisterReportActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }

    /**
     * Permissions handler
     */

    private static final int PERMISSIONS_REQUEST = 200;

    private static String[] PERMISSIONS_LIST = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private boolean permissionsGranted() {
        for (String permission : PERMISSIONS_LIST) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                for (int i = 0; i < grantResults.length; i++) {
                    Log.d(TAG, "" + grantResults[i]);
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, PERMISSIONS_LIST[i] + " permiso rechazado!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                Toast.makeText(this, "Permisos concedidos, intente nuevamente.", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Redimensionar una imagen bitmap
    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

}
