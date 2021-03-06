package hu.bme.aut.moviebase.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import hu.bme.aut.moviebase.R;
import hu.bme.aut.moviebase.data.Movie_;

public class NewMovieDialogFragment extends DialogFragment {

    public static final String TAG = "NewMovieDialogFragment";

    private EditText nameEditText;
    private Spinner categorySpinner;
    private EditText lengthEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private NumberPicker numberPicker;

    public interface NewMovieDialogListener {
        void onMovieCreated(Movie_ newMovie);
    }

    private NewMovieDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewMovieDialogListener) {
            listener = (NewMovieDialogListener) activity;
        } else {
            throw new RuntimeException(getString(R.string.exception_error));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_movie)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isValid())
                            listener.onMovieCreated(getMovie());
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private View getContentView() {
        final ViewGroup nullParent = null;
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_movie, nullParent);

        nameEditText = contentView.findViewById(R.id.MovieNameEditText);
        categorySpinner = contentView.findViewById(R.id.MovieCategorySpinner);
        categorySpinner.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.category_items)));
        lengthEditText = contentView.findViewById(R.id.MovieLengthEditText);
        descriptionEditText = contentView.findViewById(R.id.MovieDescriptionEditText);
        priceEditText = contentView.findViewById(R.id.MoviePriceEditText);
        numberPicker = contentView.findViewById(R.id.RatingPicker);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(5);
        numberPicker.setWrapSelectorWheel(true);

        return contentView;
    }

    private boolean isValid() {
        return nameEditText.getText().length() > 0;
    }

    private Movie_ getMovie() {
        Movie_ movie = new Movie_();
        movie.name = nameEditText.getText().toString();
        movie.category = Movie_.Category.getByOrdinal(categorySpinner.getSelectedItemPosition());

        try {
            movie.length = Integer.parseInt(lengthEditText.getText().toString());
        } catch (NumberFormatException e) {
            movie.length = 0;
        }

        movie.description = descriptionEditText.getText().toString();

        try {
            movie.price = Integer.parseInt(priceEditText.getText().toString());
        } catch (NumberFormatException e) {
            movie.price = 0;
        }

        movie.rating = numberPicker.getValue();
        return movie;
    }


}
