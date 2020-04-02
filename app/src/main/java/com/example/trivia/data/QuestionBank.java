package com.example.trivia.data;

import android.app.DownloadManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.controller.AppController;
import com.example.trivia.model.Question;
import static com.example.trivia.controller.AppController.TAG;

import org.json.JSONArray;
import org.json.JSONException;


import java.util.ArrayList;

public class QuestionBank {

    ArrayList<Question> questionArrayList=new ArrayList<>();
    private String url="https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public ArrayList<Question> getQuestionArrayList(final AnswerListAsyncResponse callBack) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url,
                (JSONArray) null,
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.d("abc","dfdg"+response);
                for(int i=0;i<response.length();i++)
                {
                    try {
                        Question question=new Question();
                        question.setAnswer(response.getJSONArray(i).get(0).toString());
                        question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));

                        questionArrayList.add(question);
                        Log.d("Hello","response"+question);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if(null!= callBack)
                    callBack.processFinished(questionArrayList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }
        );
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionArrayList;
    }

}
