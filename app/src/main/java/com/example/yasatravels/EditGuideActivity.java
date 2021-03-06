package com.example.yasatravels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class EditGuideActivity extends AppCompatActivity {

    public static final String EXTRA_TEXTID = "guideID";
    ImageView viewImage;
    EditText name, description, contact,email;
    Spinner district;
    Button updateBtn, removeBtn;
    String id, imageUrl;
    //    private FirebaseStorage mStorageRef;
    private DatabaseReference dbRef, deleteRef, updateRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_guide);

//        mStorageRef = FirebaseStorage.getInstance();

        updateBtn = (Button)findViewById(R.id.updateGbtn);
        removeBtn = (Button)findViewById(R.id.deleteGbtn);

        viewImage = (ImageView) findViewById(R.id.EditGuideImg);
        name = (EditText) findViewById(R.id.EditGuideName);
        email = (EditText) findViewById(R.id.EditGuideMail);
//      district = (Spinner) findViewById(R.id.EditGuideSpinner);
        description = (EditText) findViewById(R.id.EditGuideDescription);
        contact = (EditText) findViewById(R.id.EditGuideContact);

        id = getIntent().getStringExtra(EXTRA_TEXTID);

        //retrieve data
        dbRef = FirebaseDatabase.getInstance().getReference().child("Guide").child(id);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChildren())
                {
                    name.setText(snapshot.child("name").getValue().toString());
                    email.setText(snapshot.child("email").getValue().toString());
                    description.setText(snapshot.child("description").getValue().toString());
                    contact.setText(snapshot.child("contactNo").getValue().toString());
                    //set spinner--------------------------------------------------------------------------------------------
//                    district.------->snapshot.child("district").getValue().toString()
                    imageUrl = snapshot.child("image").getValue().toString();
                    Picasso.get().load(imageUrl).into(viewImage);

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No source to Display", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Delete Guide
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteRef = FirebaseDatabase.getInstance().getReference().child("Guide");

                deleteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.hasChild(id))
                        {
                            deleteRef = FirebaseDatabase.getInstance().getReference().child("Guide").child(id);
                            deleteRef.removeValue();

//                            StorageReference pref = mStorageRef.getReferenceFromUrl(imageUrl);

                            Toast.makeText(getApplicationContext(), "Deleted Succesfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(EditGuideActivity.this, ManageGuidesActivity.class);
                            startActivity(intent);
                            finish();

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "No source to Delete", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        //Update Guide Details
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateRef = FirebaseDatabase.getInstance().getReference().child("Guide").child(id);
                updateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        updateRef.child("name").setValue(name.getText().toString());
                        updateRef.child("email").setValue(email.getText().toString());
                        updateRef.child("description").setValue(description.getText().toString());
                        updateRef.child("contactNo").setValue(contact.getText().toString());
//                            updateRef.child("district").setValue(district);
//                            updateRef.child("image").setValue(url);

                        Toast.makeText(getApplicationContext(), "Updated Succesfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(EditGuideActivity.this, ManageGuidesActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
}