package in.datasciencetech.customerapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    TextView tvDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // To retrieve object
        Intent intent=getIntent();
        Location.Result result=(Location.Result) intent.getSerializableExtra("data");
        int srno=intent.getIntExtra("srno",0);
        tvDetails=(TextView)findViewById(R.id.text_view_details);
        tvDetails.setText("Sr.No. -" +String.valueOf(srno)+"\n"+
                "A/c Name -"+result.getAccountName()+"\n"+
                "Address -"+result.getAddress());
    }
}
