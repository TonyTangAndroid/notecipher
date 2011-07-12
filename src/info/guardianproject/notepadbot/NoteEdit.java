/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.guardianproject.notepadbot;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class NoteEdit extends Activity {

	private EditText mTitleText;
    private EditText mBodyText;
    private ImageView mImageView;
    private Long mRowId;
    private NotesDbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new NotesDbAdapter(this);
        
        String password = this.getIntent().getStringExtra("pwd");
        
        mDbHelper.open(password);
        setContentView(R.layout.note_edit);
        
       
        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        mImageView = (ImageView) findViewById(R.id.odata);
        
      //  Button confirmButton = (Button) findViewById(R.id.confirm);
       
        mRowId = savedInstanceState != null ? savedInstanceState.getLong(NotesDbAdapter.KEY_ROWID) 
                							: null;
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();            
			mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID) 
									: null;
			
			if (mRowId == 0)
				mRowId = null;
		}

		populateFields();
		
		/*
        confirmButton.setOnClickListener(new View.OnClickListener() {

        	public void onClick(View view) {
        	
        		
        	    setResult(RESULT_OK);
        	    finish();
        	}
          
        });*/
    }
    
    private void populateFields() {
    	try
    	{
        if (mRowId != null) {
            Cursor note = mDbHelper.fetchNote(mRowId);
            startManagingCursor(note);
            
            mTitleText.setText(note.getString(
    	            note.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
            mBodyText.setText(note.getString(
                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
            
            byte[] blob = note.getBlob(note.getColumnIndexOrThrow(NotesDbAdapter.KEY_DATA));
            
            if (blob != null)
            {
            	// Load up the image's dimensions not the image itself
				BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
				bmpFactoryOptions.inSampleSize = 2;
			
            	Bitmap blobb = BitmapFactory.decodeByteArray(blob, 0, blob.length, bmpFactoryOptions);

            	mImageView.setImageBitmap(blobb);
            }
        }   
    	}
    	catch (Exception e)
    	{
    		Log.e("notepadbot", "error populating",e);
    	}
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
       if (mRowId != null)
    		   outState.putLong(NotesDbAdapter.KEY_ROWID, mRowId);
       
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }
    
    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();

        if (title != null && title.length() > 0)
        {
	        if (mRowId == null) {
	            long id = mDbHelper.createNote(title, body, null);
	            if (id > 0) {
	                mRowId = id;
	            }
	        } else {
	            mDbHelper.updateNote(mRowId, title, body, null);
	        }
        }
        
        mDbHelper.close();
    }
    
}
