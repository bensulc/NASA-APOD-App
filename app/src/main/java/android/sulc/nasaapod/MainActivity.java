package android.sulc.nasaapod;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.sulc.nasaapod.api.retrofit;
import android.sulc.nasaapod.models.APODModel;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private SearchView mSearchView;
    private TextView mTextView;
    private TextView mCopyrightView;
    private TextView mDateView;
    private TextView mDescView ;
    private ImageView mImageView;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        mSearchView = findViewById(R.id.search_view);
        mTextView = findViewById(R.id.card_title_act);
        mCopyrightView = findViewById(R.id.card_copyright_act);
        mDateView = findViewById(R.id.card_date_act);
        mDescView = findViewById(R.id.card_explanation_act);
        mImageView = findViewById(R.id.card_image_act);
        mScrollView = findViewById(R.id.scroll);
        initButtons();
        initSearchView();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        getQuery(currentDate);
        setSupportActionBar(findViewById(R.id.toolbar));



    }

    private void initButtons() {
        Button next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {
                String query = LocalDate.parse(mDateView.getText().toString(), DateTimeFormatter.ofPattern("uuuu-MM-dd")).plusDays(1).toString();
                getQuery(query);
            }
        });

        Button previous = findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {
                String query = LocalDate.parse(mDateView.getText().toString(), DateTimeFormatter.ofPattern("uuuu-MM-dd")).plusDays(-1).toString();
                getQuery(query);
            }
        });

    }
    private void initSearchView() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onQueryTextSubmit(String query) {
                boolean valid = false;
                try {
                    LocalDate.parse(query, DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT));
                    valid = true;
                } catch(DateTimeParseException e) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid date(yyyy-MM-dd)", Toast.LENGTH_LONG).show();
                }
                if(valid) {
                    getQuery(query);

                }
                mSearchView.clearFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    public void getQuery(String date) {
        Call<APODModel> call = retrofit.getInstance().getApod().getAPOD(date, false, "frpsm4GNOzn2xHiWT7Uego5qGNea9qmu3qZ8NUWb");
        call.enqueue(new Callback<APODModel>() {
            @Override
                public void onResponse(Call<APODModel> call, Response<APODModel> response) {

                    APODModel apod = response.body();
                    try {
                        mTextView.setText(apod.title());
                        mCopyrightView.setText(apod.getCopyRight());
                        mDateView.setText(apod.getDate());
                        Picasso.get().load(apod.getUrl()).into(mImageView);
                        mDescView.setText(apod.getExplanation());
                    }
                    catch(NullPointerException e) {
                        Toast.makeText(getApplicationContext(), "Please enter a valid date (yyyy-mm-dd)", Toast.LENGTH_LONG).show();
                    }
                    mScrollView.scrollTo(0,0);



            }

            @Override
            public void onFailure(Call<APODModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occurred", Toast.LENGTH_LONG).show();

            }

        });

    }
}