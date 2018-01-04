package com.example.jiangmingyu.minilinkedin;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jiangmingyu.minilinkedin.model.BasicInfo;
import com.example.jiangmingyu.minilinkedin.model.Education;
import com.example.jiangmingyu.minilinkedin.model.Project;
import com.example.jiangmingyu.minilinkedin.util.DateUtils;
import com.example.jiangmingyu.minilinkedin.util.ImageUtils;
import com.example.jiangmingyu.minilinkedin.util.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Education education;
    private BasicInfo basicInfo;
    private Project project;
    private List<Education> educations = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();
    private static final String MODEL_EDUCATIONS = "educations";
    private static final String MODEL_EXPERIENCES = "experiences";
    private static final String MODEL_PROJECTS = "projects";
    private static final String MODEL_BASIC_INFO = "basic_info";
    private static final int REQ_CODE_EDUCATION_EDIT = 100;
    private static final int REQ_CODE_BASIC_EDIT = 101;
    private static final int REQ_CODE_PROJECT_EDIT = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();
        setUpUI();
    }

    private void setUpUI() {

        //jump to basic_info edit
//        findViewById(R.id.edit_basic_info).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, BasicInfoEditActivity.class);
//                startActivityForResult(intent, REQ_CODE_BASIC_EDIT);
//            }
//        });

        //jump to education edit
        findViewById(R.id.add_education_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDUCATION_EDIT);

            }
        });


        //jump to project edit
        findViewById(R.id.add_project_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProjectEditActivity.class);
                startActivityForResult(intent, REQ_CODE_PROJECT_EDIT);
            }
        });

        setupBasicInfo();
        setupEducations();
        //setupExperiences();
        setupProjects();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //set basic info display
        if (resultCode == Activity.RESULT_OK && requestCode == REQ_CODE_BASIC_EDIT){
            basicInfo = data.getParcelableExtra(BasicInfoEditActivity.KEY_BASIC_INFO);
            ModelUtils.save(this, MODEL_BASIC_INFO, basicInfo);
            setupBasicInfo();
        }

        //set education display
        if (resultCode == Activity.RESULT_OK && requestCode == REQ_CODE_EDUCATION_EDIT){
            String education_id = data.getStringExtra(EducationEditActivity.KEY_EDUCATION_ID);

            if (education_id != null){
                deleteEducation(education_id);
            }else{
                Education newEducation = data.getParcelableExtra(EducationEditActivity.KEY_EDUCATION_EDIT);

                boolean update = false;
                for (int i = 0; i < educations.size(); i++){
                    if (newEducation.id.equals(educations.get(i).id)){
                        educations.set(i, newEducation);
                        update = true;
                        break;
                    }
                }
                if (!update) {
                    educations.add(newEducation);
                }
                ModelUtils.save(this, MODEL_EDUCATIONS, educations);
                setupEducations();
            }

        }



        //set project display

        if (resultCode == Activity.RESULT_OK && requestCode == REQ_CODE_PROJECT_EDIT){
            String project_id = data.getStringExtra(ProjectEditActivity.KEY_PROJECT_ID);
            if (project_id != null){
                deleteProject(project_id);
            }else{
                Project newProject = data.getParcelableExtra(ProjectEditActivity.KEY_PROJECT);

                boolean update = false;
                for (int i = 0; i < projects.size(); i++){
                    if (newProject.id.equals(projects.get(i).id)){
                        projects.set(i, newProject);
                        update = true;
                        break;
                    }
                }

                if (!update){
                    projects.add(newProject);
                }
                ModelUtils.save(this, MODEL_PROJECTS, projects);
                setupProjects();
            }

        }
    }



    //basic info update display
    private void setupBasicInfo() {
        ((TextView) findViewById(R.id.name)).setText(TextUtils.isEmpty(basicInfo.name)
                ? "Your name"
                : basicInfo.name);
        ((TextView) findViewById(R.id.email)).setText(TextUtils.isEmpty(basicInfo.email)
                ? "Your email"
                : basicInfo.email);

        ImageView userPicture = (ImageView) findViewById(R.id.user_picture);
        if (basicInfo.imageUri != null) {
            ImageUtils.loadImage(this, basicInfo.imageUri, userPicture);
        } else {
            userPicture.setImageResource(R.drawable.user_ghost);
        }

        findViewById(R.id.edit_basic_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BasicInfoEditActivity.class);
                intent.putExtra(BasicInfoEditActivity.KEY_BASIC_INFO, basicInfo);
                startActivityForResult(intent, REQ_CODE_BASIC_EDIT);
            }
        });

    }

    //education info update display
    private void setupEducations() {
        LinearLayout educationsLayout = (LinearLayout) findViewById(R.id.education_list);
        educationsLayout.removeAllViews();
        for (Education education : educations) {
            View educationView = getLayoutInflater().inflate(R.layout.education_item, null);
            setupEducation(educationView, education);
            educationsLayout.addView(educationView);
        }
    }

    //draw items on the main page

    private void setupEducation(View educationView, final Education education) {
        String dateString = DateUtils.dateToString(education.startDate)
                + " - " + DateUtils.dateToString(education.endDate);
        ((TextView) educationView.findViewById(R.id.education_school))
                .setText(education.school + "   " + education.major + "   " + dateString);
        ((TextView) educationView.findViewById(R.id.education_courses))
                .setText(formatItems(education.courses));

        ImageButton editEducationBtn = (ImageButton) educationView.findViewById(R.id.edit_education_btn);
        editEducationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                intent.putExtra(EducationEditActivity.KEY_EDUCATION_EDIT, education);
                startActivityForResult(intent, REQ_CODE_EDUCATION_EDIT);
            }
        });
    }
    //project info update display
    private void setupProjects(){
        LinearLayout projectsLayout = findViewById(R.id.project_list);
        projectsLayout.removeAllViews();
        for (Project project : projects){
            View projectView = getLayoutInflater().inflate(R.layout.project_item, null);
            setupProject(projectView, project);
            projectsLayout.addView(projectView);
        }
    }

    private void setupProject(View projectView, final Project project) {
        String dateString = DateUtils.dateToString(project.startDate) +
                " - " + DateUtils.dateToString(project.endDate);
        ((TextView) projectView.findViewById(R.id.project_name)).setText(project.name + "   "  + dateString);
        ((TextView) projectView.findViewById(R.id.project_details))
                .setText(formatItems(project.details));

        ImageButton editProjectBtn = (ImageButton) projectView.findViewById(R.id.edit_project_btn);
        editProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProjectEditActivity.class);
                intent.putExtra(ProjectEditActivity.KEY_PROJECT, project);
                startActivityForResult(intent, REQ_CODE_PROJECT_EDIT);
            }
        });
    }

    private void deleteEducation(@NonNull String educationId) {
        for (int i = 0; i < educations.size(); ++i) {
            Education e = educations.get(i);
            if (TextUtils.equals(e.id, educationId)) {
                educations.remove(i);
                break;
            }
        }

        ModelUtils.save(this, MODEL_EDUCATIONS, educations);
        setupEducations();
    }

    private void deleteProject(String project_id) {
        for (int i = 0; i < projects.size(); ++i) {
            Project p = projects.get(i);
            if (TextUtils.equals(p.id, project_id)) {
                projects.remove(i);
                break;
            }
        }
        ModelUtils.save(this, MODEL_PROJECTS, projects);
        setupProjects();
    }

    private void loadData() {
        BasicInfo savedBasicInfo = ModelUtils.read(this,
                MODEL_BASIC_INFO,
                new TypeToken<BasicInfo>(){});
        basicInfo = savedBasicInfo == null ? new BasicInfo() : savedBasicInfo;

        List<Education> savedEducation = ModelUtils.read(this,
                MODEL_EDUCATIONS,
                new TypeToken<List<Education>>(){});
        educations = savedEducation == null ? new ArrayList<Education>() : savedEducation;

//        List<Experience> savedExperience = ModelUtils.read(this,
//                MODEL_EXPERIENCES,
//                new TypeToken<List<Experience>>(){});
//        experiences = savedExperience == null ? new ArrayList<Experience>() : savedExperience;

        List<Project> savedProjects = ModelUtils.read(this,
                MODEL_PROJECTS,
                new TypeToken<List<Project>>(){});
        projects = savedProjects == null ? new ArrayList<Project>() : savedProjects;
    }
    public static String formatItems(List<String> items) {
        StringBuilder sb = new StringBuilder();
        for (String item: items) {
            sb.append(' ').append('·').append(' ').append(item).append('\n');
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }


}