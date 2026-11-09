package com.PouyaApp.KookYaRSantooR;

import com.kookyar.common.KookyarFonts;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;

/**
 * Builds the card lists behind the kook and piece pickers.
 *
 * These screens used to be columns of PNG buttons on a tiled background; they
 * now share the card look of the book shelf.
 */
final class PickList {

    private final Activity activity;
    private final LinearLayout container;
    private final LayoutInflater inflater;
    private final Typeface face;

    PickList(Activity activity, String fontAsset) {
        this.activity = activity;
        this.container = activity.findViewById(R.id.pick_container);
        this.inflater = LayoutInflater.from(activity);
        this.face = KookyarFonts.get(activity, fontAsset);
    }

    void clear() {
        container.removeAllViews();
    }

    void addHeader(String text) {
        View header = inflater.inflate(R.layout.item_pick_header, container, false);
        TextView label = header.findViewById(R.id.pick_header);
        label.setText(text);
        label.setTypeface(face);
        container.addView(header);
    }

    void addCard(String title, @ColorRes int color, @DrawableRes int icon, Runnable action) {
        addCard(title, null, color, icon, action);
    }

    void addCard(String title, String subtitle, @ColorRes int color, @DrawableRes int icon,
                 Runnable action) {
        View card = inflater.inflate(R.layout.item_pick_card, container, false);
        ((MaterialCardView) card).setCardBackgroundColor(
                ContextCompat.getColor(activity, color));

        TextView titleView = card.findViewById(R.id.pick_title);
        titleView.setText(title);
        titleView.setTypeface(face);

        TextView subtitleView = card.findViewById(R.id.pick_subtitle);
        if (subtitle == null) {
            subtitleView.setVisibility(View.GONE);
        } else {
            subtitleView.setVisibility(View.VISIBLE);
            subtitleView.setText(subtitle);
            subtitleView.setTypeface(face);
        }

        ImageView iconView = card.findViewById(R.id.pick_icon);
        iconView.setImageResource(icon);
        iconView.setImageTintList(ContextCompat.getColorStateList(activity, R.color.white));

        card.setOnClickListener(v -> action.run());
        container.addView(card);
    }
}
