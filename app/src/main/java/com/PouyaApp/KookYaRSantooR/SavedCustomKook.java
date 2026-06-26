package com.PouyaApp.KookYaRSantooR;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

final class SavedCustomKook {

    static final int NOTE_COUNT = 27;
    private static final String PREFS_NAME = "saved_custom_kooks";
    private static final String KEY_COUNT = "count";
    private static final int MAX_SAVED = 10;

    private SavedCustomKook() {
    }

    static final class Entry {
        final String name;
        final double[] miditones;

        Entry(String name, double[] miditones) {
            this.name = name;
            this.miditones = miditones;
        }
    }

    static List<Entry> loadAll(Context context) {
        SharedPreferences prefs = prefs(context);
        int count = prefs.getInt(KEY_COUNT, 0);
        List<Entry> entries = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String name = prefs.getString(nameKey(i), null);
            String data = prefs.getString(dataKey(i), null);
            if (name == null || data == null) {
                continue;
            }
            entries.add(new Entry(name, decode(data)));
        }
        return entries;
    }

    static boolean save(Context context, String name, double[] miditones) {
        if (name == null || name.trim().isEmpty() || miditones == null
                || miditones.length != NOTE_COUNT) {
            return false;
        }
        String trimmedName = name.trim();
        SharedPreferences prefs = prefs(context);
        List<Entry> entries = loadAll(context);
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).name.equals(trimmedName)) {
                writeEntry(prefs, i, trimmedName, miditones);
                prefs.edit().putInt(KEY_COUNT, entries.size()).apply();
                return true;
            }
        }
        if (entries.size() >= MAX_SAVED) {
            return false;
        }
        int index = entries.size();
        writeEntry(prefs, index, trimmedName, miditones);
        prefs.edit().putInt(KEY_COUNT, index + 1).apply();
        return true;
    }

    static void delete(Context context, int index) {
        List<Entry> entries = loadAll(context);
        if (index < 0 || index >= entries.size()) {
            return;
        }
        entries.remove(index);
        SharedPreferences prefs = prefs(context);
        SharedPreferences.Editor editor = prefs.edit();
        for (int i = 0; i < entries.size(); i++) {
            writeEntry(editor, i, entries.get(i).name, entries.get(i).miditones);
        }
        for (int i = entries.size(); i < MAX_SAVED; i++) {
            editor.remove(nameKey(i));
            editor.remove(dataKey(i));
        }
        editor.putInt(KEY_COUNT, entries.size()).apply();
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private static void writeEntry(SharedPreferences prefs, int index, String name, double[] miditones) {
        writeEntry(prefs.edit(), index, name, miditones).apply();
    }

    private static SharedPreferences.Editor writeEntry(
            SharedPreferences.Editor editor, int index, String name, double[] miditones) {
        return editor
                .putString(nameKey(index), name)
                .putString(dataKey(index), encode(miditones));
    }

    private static String nameKey(int index) {
        return "name_" + index;
    }

    private static String dataKey(int index) {
        return "data_" + index;
    }

    private static String encode(double[] miditones) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < miditones.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(miditones[i]);
        }
        return builder.toString();
    }

    private static double[] decode(String value) {
        String[] parts = value.split(",");
        double[] miditones = new double[NOTE_COUNT];
        for (int i = 0; i < NOTE_COUNT; i++) {
            if (i < parts.length) {
                miditones[i] = Double.parseDouble(parts[i]);
            }
        }
        return miditones;
    }
}
