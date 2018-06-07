package mchenys.net.csdn.blog.testscroll;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mchenys.net.csdn.blog.testscroll.demo1.Demo1Activity;
import mchenys.net.csdn.blog.testscroll.demo2.Demo2Activity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toDemo1(View view) {
        startActivity(new Intent(this, Demo1Activity.class));
    }
    public void toDemo2(View view) {
        startActivity(new Intent(this, Demo2Activity.class));
    }
}
