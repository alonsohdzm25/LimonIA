// Generated by view binder compiler. Do not edit!
package com.limonia.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import com.limonia.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemPlagaBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final ImageView imgViewPlaga;

  @NonNull
  public final TextView textViewPlaga;

  private ItemPlagaBinding(@NonNull CardView rootView, @NonNull ImageView imgViewPlaga,
      @NonNull TextView textViewPlaga) {
    this.rootView = rootView;
    this.imgViewPlaga = imgViewPlaga;
    this.textViewPlaga = textViewPlaga;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemPlagaBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemPlagaBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_plaga, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemPlagaBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.imgViewPlaga;
      ImageView imgViewPlaga = rootView.findViewById(id);
      if (imgViewPlaga == null) {
        break missingId;
      }

      id = R.id.textViewPlaga;
      TextView textViewPlaga = rootView.findViewById(id);
      if (textViewPlaga == null) {
        break missingId;
      }

      return new ItemPlagaBinding((CardView) rootView, imgViewPlaga, textViewPlaga);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}