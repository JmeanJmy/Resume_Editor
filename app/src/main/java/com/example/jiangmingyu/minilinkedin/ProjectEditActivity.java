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

import com.example.jiangmingyu.minilinkedin.model.Project;
import com.example.jiangmingyu.minilinkedin.util.DateUtils;

import java.util.Arrays;

public class ProjectEditActivity extends AppCompatActivity {

    public static final String KEY_PROJECT = "project";
    public static final String KEY_PROJECT_ID = "project_id";
    private Project project;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        project = getIntent().getParcelableExtra(KEY_PROJECT);
        if (project != null){
            setupUI(project);
        }
    }

    private void setupUI(final Project project) {
        ((TextView) findViewById(R.id.project_edit_name)).setText(project.name);
        ((TextView) findViewById(R.id.project_edit_start_date)).setText(
                DateUtils.dateToString(project.startDate));
        ((TextView) findViewById(R.id.project_edit_end_date)).setText(
                DateUtils.dateToString(project.endDate));
        ((TextView) findViewById(R.id.project_edit_details)).setText(TextUtils.join("\n", project.details));
        findViewById(R.id.project_edit_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                findViewById(R.id.project_edit_delete).setVisibility(View.GONE);
                resultIntent.putExtra(KEY_PROJECT_ID, project.id);
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
        }
        else if (item.getItemId() == R.id.action_save){
            saveAndExit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAndExit() {
        if (project == null){
            project = new Project();
        }

        project.name = ((EditText) findViewById(R.id.project_edit_name)).getText().toString();
        project.startDate = DateUtils.stringToDate(
                ((TextView) findViewById(R.id.project_edit_start_date)).getText().toString());
        project.endDate = DateUtils.stringToDate(
                ((TextView) findViewById(R.id.project_edit_end_date)).getText().toString());
        project.details = Arrays.asList(TextUtils.split(
                ((EditText) findViewById(R.id.project_edit_details)).getText().toString(), "\n"));

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_PROJECT, project);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
