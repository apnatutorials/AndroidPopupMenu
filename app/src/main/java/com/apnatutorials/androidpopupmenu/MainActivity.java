package com.apnatutorials.androidpopupmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private static String POPUP_CONSTANT = "mPopup";
    private static String POPUP_FORCE_SHOW_ICON = "setForceShowIcon";
    String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    ArrayAdapter<String> adapter;
    ListView lvDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, days);
        lvDays = (ListView) findViewById(R.id.lvDays);
        lvDays.setAdapter(adapter);
        lvDays.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lvDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                lvDays.setItemChecked(position, true);
                showPopup(view);
            }
        });
    }

    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(MainActivity.this, view);
        try {
            // Reflection apis to enforce show icon
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals(POPUP_CONSTANT)) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(POPUP_FORCE_SHOW_ICON, boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String selectedItem = adapter.getItem(lvDays.getCheckedItemPosition());
        switch (item.getItemId()) {
            case R.id.pmnuDelete:
                Toast.makeText(getApplicationContext(), "You clicked delete on Item : " + selectedItem, Toast.LENGTH_SHORT).show();
                break;
            case R.id.pmnuEdit:
                Toast.makeText(getApplicationContext(), "You clicked edit on Item : " + selectedItem, Toast.LENGTH_SHORT).show();
                break;
            case R.id.pmnuShare:
                Toast.makeText(getApplicationContext(), "You clicked share on Item : " + selectedItem, Toast.LENGTH_SHORT).show();
                break;
        }

        return false;
    }
}
