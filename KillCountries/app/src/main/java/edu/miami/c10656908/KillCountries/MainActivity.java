package edu.miami.c10656908.KillCountries;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity
        implements AdapterView.OnItemClickListener {

    MySimpleAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillGrid();

        //theGrid = (GridView) findViewById(R.id.the_grid);
        //theGrid.setOnItemClickListener(this);
    }


    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long rowId) {

        view.findViewById(R.id.country_text).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.country_picture).setVisibility(View.VISIBLE);
        gridAdapter.setDisplayImage(position);

        //TextView textView = (TextView)view.findViewById(R.id.country_text);
        //textView.setVisibility(view.INVISIBLE);
        //view.setVisibility(view.INVISIBLE);


        //view.findViewById(R.id.country_picture).setVisibility(View.VISIBLE);

        /*switch (view.getId()){
            case R.id.country_text:
                ImageView smily = null;
                ViewGroup gridUnit = (ViewGroup) view.getParent();
                for(int itemPosition = 0; itemPosition < gridUnit.getChildCount(); itemPosition++) {
                    View checkView = gridUnit.getChildAt(itemPosition);
                    if(checkView instanceof ImageView) {
                        smily = (ImageView) checkView;
                        break;
                    }

                }
                view.setVisibility(View.GONE);
                smily.setVisibility(View.VISIBLE);
                break;
            case R.id.country_picture:
                break;
            default:
                break;
        }

    */
    }
    public void fillGrid() {
        String[] countries;
        HashMap<String, Object> oneItem;
        ArrayList<HashMap<String, Object>> gridItems;
        GridView theGrid;

        int index;
        String[] fromHashMapFieldNames = {"name"};
        int[] toListRowFieldIds = {R.id.country_text};

        countries = getResources().getStringArray(R.array.countries_array);
        gridItems = new ArrayList<HashMap<String, Object>>();
        for(index = 0; index < countries.length; index++) {
            oneItem = new HashMap<String,Object>();
            oneItem.put("name", countries[index]);
            //oneItem.put("picture", R.id.country_picture);
            gridItems.add(oneItem);
        }
        gridAdapter = new MySimpleAdapter(this, gridItems,
                R.layout.ui_adapter_gridview_list_item, fromHashMapFieldNames, toListRowFieldIds);
        theGrid = (GridView)findViewById(R.id.the_grid);
        theGrid.setAdapter(gridAdapter);
    }
    public void myClickHandler(View view) {
        TextView textView = (TextView)view;
        view.setVisibility(View.INVISIBLE);

        ImageView smily = null;
        ViewGroup gridUnit = (ViewGroup) view.getParent();
        for(int itemPosition = 0; itemPosition < gridUnit.getChildCount(); itemPosition++) {
            View checkView = gridUnit.getChildAt(itemPosition);
            if(checkView instanceof ImageView) {
                smily = (ImageView) checkView;
                smily.setVisibility(View.VISIBLE);
                break;
            }

        }
        view.setVisibility(View.GONE);
        smily.setVisibility(View.VISIBLE);



    }
}


 class MySimpleAdapter extends SimpleAdapter {
    //-----------------------------------------------------------------------------
    boolean[] displayImage;

    //-----------------------------------------------------------------------------
    public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data,
                           int resource, String[] keyNames, int[] fieldIds) {

        super(context, data, resource, keyNames, fieldIds);

        displayImage = new boolean[getCount()];
        Arrays.fill(displayImage, false);
    }

    //-----------------------------------------------------------------------------
    public void setDisplayImage(int position) {

        displayImage[position] = true;
    }

    //-----------------------------------------------------------------------------
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        TextView name;
        ImageView smiley;

//----Let the superclass decide whether or not to recycle
        view = super.getView(position, convertView, parent);
        name = (TextView) view.findViewById(R.id.country_text);
        smiley = (ImageView) view.findViewById(R.id.country_picture);
        if (displayImage[position]) {
            //name.setVisibility(View.INVISIBLE);
            //smiley.setVisibility(View.VISIBLE);
        } else {
            //name.setVisibility(View.VISIBLE);
            //smiley.setVisibility(View.INVISIBLE);
        }
        return (view);
    }
//-----------------------------------------------------------------------------
}
