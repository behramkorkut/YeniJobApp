package com.yenimobile.yenijobapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobWorkItem;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static String id1 = "test_channel_01";

    JobScheduler jobScheduler;
    JobInfo job;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createChannel();
        //get the Job Scheduler
        jobScheduler = getSystemService(JobScheduler.class);
        //get the job we will be sending workitems too.
        job =getJobInfo();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //let's create 3 fake work items and enqueue them.
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    JobWorkItem item;
                    Intent i;
                    //first item
                    i = new Intent();
                    i.putExtra("Key1", "first item"); //fake data too.
                    item = new JobWorkItem(i);
                    jobScheduler.enqueue(job,item);
                    //second item
                    i = new Intent();
                    i.putExtra("Key1", "Second item"); //fake data too.
                    item = new JobWorkItem(i);
                    jobScheduler.enqueue(job,item);
                    //first item
                    i = new Intent();
                    i.putExtra("Key1", "Third item"); //fake data too.
                    item = new JobWorkItem(i);
                    jobScheduler.enqueue(job,item);
                }else {
                    Toast.makeText(MainActivity.this,
                            "sorry, needs Android Oreo for JobWorkItem",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //This is a helper method to schedule the job.  It doesn't need to be declared here, but it
    //a good way to keep everything together.

    // schedule the start of the service every 10 - 30 seconds
    public JobInfo getJobInfo() {

        ComponentName serviceComponent = new ComponentName(getApplicationContext(), MyJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(10 * 1000); // wait at least
        builder.setOverrideDeadline(30 * 1000); // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        //builder.setRequiresBatteryNotLow(true);  //only when the batter is not low.  API 26+

        return builder.build();
    }

    /*
     * Min Requirement for this example is API 26+, so no wrapper needed to for channel creation.
     */

    private void createChannel() {

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel mChannel = new NotificationChannel(id1,
                    getString(R.string.channel_name),  //name of the channel
                    NotificationManager.IMPORTANCE_LOW);   //importance level
            //important level: default is is high on the phone.  high is urgent on the phone.  low is medium, so none is low?
            // Configure the notification channel.
            mChannel.setDescription(getString(R.string.channel_description));
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this channel, if the device supports this feature.
            mChannel.setShowBadge(true);
            nm.createNotificationChannel(mChannel);
        }



    }
}