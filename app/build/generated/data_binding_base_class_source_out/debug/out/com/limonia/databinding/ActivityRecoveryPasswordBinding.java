// Generated by view binder compiler. Do not edit!
package com.limonia.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import com.limonia.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRecoveryPasswordBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnAceptar;

  @NonNull
  public final Button btnCancelar;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final ProgressBar progressBar;

  @NonNull
  public final EditText txtPassword;

  @NonNull
  public final EditText txtPasswordRepeat;

  @NonNull
  public final TextView txtTitle;

  private ActivityRecoveryPasswordBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button btnAceptar, @NonNull Button btnCancelar, @NonNull ImageView imageView,
      @NonNull ProgressBar progressBar, @NonNull EditText txtPassword,
      @NonNull EditText txtPasswordRepeat, @NonNull TextView txtTitle) {
    this.rootView = rootView;
    this.btnAceptar = btnAceptar;
    this.btnCancelar = btnCancelar;
    this.imageView = imageView;
    this.progressBar = progressBar;
    this.txtPassword = txtPassword;
    this.txtPasswordRepeat = txtPasswordRepeat;
    this.txtTitle = txtTitle;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRecoveryPasswordBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRecoveryPasswordBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_recovery_password, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRecoveryPasswordBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnAceptar;
      Button btnAceptar = rootView.findViewById(id);
      if (btnAceptar == null) {
        break missingId;
      }

      id = R.id.btnCancelar;
      Button btnCancelar = rootView.findViewById(id);
      if (btnCancelar == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = rootView.findViewById(id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.progress_bar;
      ProgressBar progressBar = rootView.findViewById(id);
      if (progressBar == null) {
        break missingId;
      }

      id = R.id.txtPassword;
      EditText txtPassword = rootView.findViewById(id);
      if (txtPassword == null) {
        break missingId;
      }

      id = R.id.txtPasswordRepeat;
      EditText txtPasswordRepeat = rootView.findViewById(id);
      if (txtPasswordRepeat == null) {
        break missingId;
      }

      id = R.id.txtTitle;
      TextView txtTitle = rootView.findViewById(id);
      if (txtTitle == null) {
        break missingId;
      }

      return new ActivityRecoveryPasswordBinding((ConstraintLayout) rootView, btnAceptar,
          btnCancelar, imageView, progressBar, txtPassword, txtPasswordRepeat, txtTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
