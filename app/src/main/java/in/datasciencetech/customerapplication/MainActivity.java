package in.datasciencetech.customerapplication;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RowListner {

    //ListView listView;
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    List<Location.Result> results = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //listView = (ListView) findViewById(R.id.listViewHeroes);
        recyclerView = findViewById(R.id.recycler_view_loc_list);
        if (isInternetAvailable()){
            //calling the method to display the heroes
            getData();
    }else{
            Toast.makeText(getApplicationContext(), "Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        ApiClient api = retrofit.create(ApiClient.class);

        Call<Location> call = api.getHeroes();

        call.enqueue(new Callback<Location>() {
            @Override
            public void onResponse(Call<Location> call, Response<Location> response) {
                Location locList = response.body();

                List<Location.Result> results1 = locList.getResult();


                results = locList.getResult();
                for (int i = 0; i < results.size(); i++) {
                    if (!results.get(i).getLatitude().equals("") || !results.get(i).getLatitude().equals("")) {
                        String address = getAddress(Double.valueOf(results.get(i).getLatitude()), Double.valueOf(results.get(i).getLongitude()));

                        results.get(i).setAddress(address);
                    } else {
                        results.get(i).setAddress("Invalid LatLong");
                    }


                }
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerAdapter = new RecyclerAdapter(MainActivity.this, results);
                recyclerAdapter.setClickListener(MainActivity.this);
                recyclerView.setAdapter(recyclerAdapter);


            }

            @Override
            public void onFailure(Call<Location> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {


        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        //To pass:
        intent.putExtra("data", results.get(position));
        intent.putExtra("srno", position);
        startActivity(intent);

    }

    @Override
    public void onViewButtonClick(View view, int position) {
       // String uri = "http://maps.google.co.in/maps?q=" + results.get(position).getAddress();

        if (!results.get(position).getLatitude().equals("") || !results.get(position).getLatitude().equals("")) {

            Double myLatitude = Double.valueOf(results.get(position).getLatitude());
            Double myLongitude = Double.valueOf(results.get(position).getLongitude());
            String labelLocation = results.get(position).getAccountName();

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + myLatitude  + ">,<" + myLongitude + ">?q=<" + myLatitude  + ">,<" + myLongitude + ">(" + labelLocation + ")"));
            startActivity(intent);


//            String uri = String.format(Locale.ENGLISH, "geo:%f,%f",Double.valueOf(results.get(position).getLatitude()),Double.valueOf(results.get(position).getLongitude()));
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//        startActivity(intent);
        }
    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(addresses.get(0).getAddressLine(0)).append(" ");
                if (addresses.get(0).getAddressLine(1) != null) {
                    result.append(addresses.get(0).getAddressLine(1)).append("\n");
                }
                if (address.getLocality() != null) {
                    result.append(address.getLocality()).append("\n");
                }
                if (address.getCountryName() != null) {
                    result.append(address.getCountryName()).append("\n");
                }
                if (addresses.get(0).getPostalCode() != null) {
                    result.append(addresses.get(0).getPostalCode()).append("\n");
                }
                if (addresses.get(0).getFeatureName() != null) {
                    result.append(addresses.get(0).getFeatureName());
                }
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

//    public boolean isInternetAvailable() {
//        try {
//            InetAddress ipAddr = InetAddress.getByName("google.com");
//            //You can replace it with your name
//            return !ipAddr.equals("");
//
//        } catch (Exception e) {
//            return false;
//        }
//    }
    public boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
