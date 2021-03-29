package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.api.NasaApiStatus
import com.udacity.asteroidradar.main.AsteroidAdapter
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay

@BindingAdapter("pictureOfTheDay")
fun bindPictureOfTheDay(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    Picasso.get()
            .load(pictureOfDay?.url)
            .placeholder(R.drawable.placeholder_picture_of_day)
            .into(imageView)
}

@BindingAdapter("asteroidsList")
fun bindAsteroidsList(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidAdapter
    adapter.submitList(data)
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidCodename")
fun bindAsteroidCodename(textView: TextView, codeName: String?) {
    textView.text = codeName ?: "Asteroid"
}

@BindingAdapter("asteroidCloseApproachDate")
fun bindAsteroidCloseApproachDate(textView: TextView, closeApproachDate: String?) {
    textView.text = closeApproachDate ?: ""
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("nasaApiStatus")
fun bindProgressBarToNasaApiStatus(progressBar: ProgressBar, nasaApiStatus: NasaApiStatus?){

    when(nasaApiStatus) {
        NasaApiStatus.LOADING -> {
            progressBar.visibility = View.VISIBLE
        }

        NasaApiStatus.ERROR -> {
            progressBar.visibility = View.GONE
            Toast.makeText(progressBar.context, R.string.nasa_api_status_error, Toast.LENGTH_LONG).show()
        }

        NasaApiStatus.DONE -> {
            progressBar.visibility = View.GONE
        }
    }
}
