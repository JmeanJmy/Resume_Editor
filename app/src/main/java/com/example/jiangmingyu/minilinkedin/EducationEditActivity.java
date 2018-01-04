package com.example.jiangmingyu.minilinkedin;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.example.jiangmingyu.minilinkedin.util.DateUtils;
import com.example.jiangmingyu.minilinkedin.model.Education;
import java.util.Arrays;



public class EducationEditActivity extends AppCompatActivity {
    public static final String KEY_EDUCATION_EDIT = "education";
    public static final String KEY_EDUCATION_ID = "education_id";
    private Education education;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        education = getIntent().getParcelableExtra(KEY_EDUCATION_EDIT);
        if (education != null){
            setUpUI(education);
        }

    }

    private void setUpUI(final Education education) {
        ((TextView) findViewById(R.id.education_edit_school)).setText(education.school);
        ((TextView) findViewById(R.id.education_edit_major)).setText(education.major);
        ((TextView) findViewById(R.id.education_edit_start_date)).setText(
                DateUtils.dateToString(education.startDate));
        ((TextView) findViewById(R.id.education_edit_end_date)).setText(
                DateUtils.dateToString(education.endDate));

        ((TextView) findViewById(R.id.education_edit_courses)).setText(TextUtils.join("\n", education.courses));

        findViewById(R.id.education_edit_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                findViewById(R.id.education_edit_delete).setVisibility(View.GONE);
                resultIntent.putExtra(KEY_EDUCATION_ID, education.id);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }else if (item.getItemId() == R.id.action_save){
            saveAndExit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAndExit() {
        if (education == null){
            education = new Education();
        }

        education.school = ((EditText) findViewById(R.id.education_edit_school)).getText().toString();
        education.major = ((EditText) findViewById(R.id.education_edit_major)).getText().toString();
        education.startDate = DateUtils.stringToDate(
                ((TextView) findViewById(R.id.education_edit_start_date)).getText().toString());
        education.endDate = DateUtils.stringToDate(
                ((TextView) findViewById(R.id.education_edit_end_date)).getText().toString());
        education.courses = Arrays.asList(TextUtils.split(
                ((EditText) findViewById(R.id.education_edit_courses)).getText().toString(), "\n"));

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_EDUCATION_EDIT, education);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
