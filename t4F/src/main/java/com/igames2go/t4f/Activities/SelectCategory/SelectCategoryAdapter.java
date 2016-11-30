
package com.igames2go.t4f.Activities.SelectCategory;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.Activities.PlayerStatActivity;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.SelCategoriesDataObject;

import com.igames2go.t4f.tasks.SaveCatFavorite;
import com.igames2go.t4f.tasks.SavePurchase;
import com.igames2go.t4f.utils.ImageLoader;

import java.util.ArrayList;
import java.util.TreeSet;

public class SelectCategoryAdapter extends BaseAdapter {

    private int resource;
    private LayoutInflater inflater;
    private Activity context;
    private ImageLoader loader;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPERATOR = 1;

    private ArrayList<SelCategoriesDataObject> items = new ArrayList<SelCategoriesDataObject>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private ArrayList<String> collapsedGroupIds;
    private ArrayList<SelCategoriesDataObject> groupCategories;
    private ArrayList<SelCategoriesDataObject> favorCategories;
    private ArrayList<SelCategoriesDataObject> categories;

    private final String TAG = "SelectCategoryAdapter";

    public SelectCategoryAdapter(Activity ctx, int resourceId) {

        loader = new ImageLoader(ctx);
        resource = resourceId;
        inflater = LayoutInflater.from(ctx);
        context = ctx;

        collapsedGroupIds = new ArrayList<String>();
        groupCategories = new ArrayList<SelCategoriesDataObject>();
        favorCategories = new ArrayList<SelCategoriesDataObject>();
        categories = new ArrayList<SelCategoriesDataObject>();
        items = new ArrayList<SelCategoriesDataObject>();
    }

    public void addItem(final SelCategoriesDataObject item) {

        if (item.getSecion() == SelCategoriesDataObject.SECTION_GROUP)
            groupCategories.add(item);
        else if(item.getSecion() == SelCategoriesDataObject.SECTION_FAVOR)
            favorCategories.add(item);
        else
            categories.add(item);

        if (item.getExpandflag() == SelCategoriesDataObject.EXPAND_FLAG_PLUS || item.getExpandflag() == SelCategoriesDataObject.EXPAND_FLAG_BUY) {
            if (item.isExpanded() == false)
                collapsedGroupIds.add(item.getGroupid());
            items.add(item);
        }else{
            if(collapsedGroupIds.indexOf(item.getGroupid()) == -1)
                items.add(item);
        }

        notifyDataSetChanged();
    }

    public void addItem(final SelCategoriesDataObject item, int index) {
        items.add(index, item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final SelCategoriesDataObject item) {
        items.add(item);
        sectionHeader.add(items.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position){
        return  sectionHeader.contains(position) ? TYPE_SEPERATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount(){
        return  2;
    }

    @Override
    public int getCount(){
        return  items.size();
    }

    @Override
    public SelCategoriesDataObject getItem(int position){
        return  items.get(position);
    }

    @Override
    public long getItemId(int position){
        return  position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

    	final SelCategoriesDataObject obj = items.get(position);
        Holder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new Holder();

            switch (rowType) {
                case TYPE_ITEM:
                    convertView = (LinearLayout) inflater.inflate(resource, null);
                    holder.ivCatImage = (ImageView) convertView.findViewById(R.id.ivCatImage);
                    holder.txtGroupTitle = (TextView) convertView.findViewById(R.id.txtGroupTitle);
                    holder.txtExpand = (TextView)convertView.findViewById(R.id.txtExapnd);

                    holder.btnFavorite = (ImageButton) convertView.findViewById(R.id.btnFavorite);
                    holder.txtFavorPrice = (TextView)convertView.findViewById(R.id.txtFavorPrice);
                    holder.btnBuy = (ImageButton) convertView.findViewById(R.id.btnBuy);
                    holder.cbSelect = (CheckBox) convertView.findViewById(R.id.cbSelect);
                    holder.btnDetails = (ImageButton) convertView.findViewById(R.id.btnDetails);

                    holder.titleLayout = (LinearLayout)convertView.findViewById(R.id.titleLayout);
                    holder.favorLayout = (RelativeLayout) convertView.findViewById(R.id.favorLayout);
                    holder.actionLayout = (RelativeLayout) convertView.findViewById(R.id.actionLayout);
                    holder.disclosureLayout = (RelativeLayout) convertView.findViewById(R.id.disclosureLayout);

                    break;
                case TYPE_SEPERATOR:
                    convertView = (LinearLayout) inflater.inflate(R.layout.layout_selectcategorylistheader, null);
                    holder.txtSectionTitle = (TextView) convertView.findViewById(R.id.txtSectionTitle);
                    break;
            }

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        switch (rowType) {
            case TYPE_ITEM:

                holder.favorLayout.setVisibility(View.VISIBLE);
                holder.actionLayout.setVisibility(View.VISIBLE);

                if(obj.getSecion() == SelCategoriesDataObject.SECTION_CATEGORY || obj.getSecion() == SelCategoriesDataObject.SECTION_FAVOR){

                    holder.txtExpand.setVisibility(View.GONE);
                    holder.txtGroupTitle.setVisibility(View.GONE);
                    holder.ivCatImage.setVisibility(View.VISIBLE);

                    loader.DisplayImage(
                            context.getString(R.string.url_category) + obj.getImagewide(),
                            holder.ivCatImage, false);
                }else{

                    if(obj.getExpandflag() == SelCategoriesDataObject.EXPAND_FLAG_NO){

                        holder.txtExpand.setVisibility(View.INVISIBLE);
                        holder.txtGroupTitle.setVisibility(View.GONE);
                        holder.ivCatImage.setVisibility(View.VISIBLE);

                        loader.DisplayImage(
                                context.getString(R.string.url_category) + obj.getImagewide(),
                                holder.ivCatImage, false);

                    }else if (obj.getExpandflag() == SelCategoriesDataObject.EXPAND_FLAG_BUY){

                        holder.txtExpand.setVisibility(View.GONE);
                        holder.txtGroupTitle.setVisibility(View.VISIBLE);
                        holder.ivCatImage.setVisibility(View.GONE);

                        holder.txtGroupTitle.setText(obj.getGroupname());
                    }else if(obj.getExpandflag() == SelCategoriesDataObject.EXPAND_FLAG_PLUS){

                        holder.txtExpand.setVisibility(View.VISIBLE);
                        holder.txtGroupTitle.setVisibility(View.VISIBLE);
                        holder.ivCatImage.setVisibility(View.GONE);

                        if (obj.isExpanded())
                            holder.txtExpand.setText("-");
                        else
                            holder.txtExpand.setText("+");

                        holder.txtGroupTitle.setText(obj.getGroupname());
                        holder.favorLayout.setVisibility(View.INVISIBLE);
                        holder.actionLayout.setVisibility(View.INVISIBLE);
                    }else {

                    }
                }

                if (obj.getExpandflag() != SelCategoriesDataObject.EXPAND_FLAG_PLUS){

                    if (obj.getFavor() == SelCategoriesDataObject.FAVOR_EMPTY){
                        holder.txtFavorPrice.setVisibility(View.VISIBLE);
                        holder.btnFavorite.setVisibility(View.GONE);

                        holder.txtFavorPrice.setText(obj.getFavorprice());
                    }else{

                        holder.txtFavorPrice.setVisibility(View.GONE);
                        holder.btnFavorite.setVisibility(View.VISIBLE);


                        if (obj.getFavor() == SelCategoriesDataObject.FAVOR_YES)
                            holder.btnFavorite.setImageResource(R.drawable.icon_star_filled);
                        else
                            holder.btnFavorite.setImageResource(R.drawable.icon_star_empty);

                    }

                    if (obj.getAction() != SelCategoriesDataObject.ACTION_EMPTY){
                        if (obj.getAction() == SelCategoriesDataObject.ACTION_BUY){

                            holder.btnBuy.setVisibility(View.VISIBLE);
                            holder.cbSelect.setVisibility(View.GONE);

                        }else{

                            holder.btnBuy.setVisibility(View.GONE);
                            holder.cbSelect.setVisibility(View.VISIBLE);

                            holder.cbSelect.setOnCheckedChangeListener(null);

                            if (obj.getAction() == SelCategoriesDataObject.ACTION_SELECT)
                                holder.cbSelect.setChecked(true);
                            else
                                holder.cbSelect.setChecked(false);
                        }
                    }
                }

                if (obj.getDisclosureurl().length() > 0){
                    holder.btnDetails.setVisibility(View.VISIBLE);
                }else{
                    holder.btnDetails.setVisibility(View.GONE);
                }

                holder.btnDetails.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(context, SelectCategoryDetailsActivity.class);
                        intent.putExtra("title", obj.getGroupname());
                        intent.putExtra("url", obj.getDisclosureurl());
                        context.startActivity(intent);
                    }
                });

                holder.btnFavorite.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        setCatFavorite(obj);
                    }
                });

                holder.titleLayout.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(obj.getExpandflag() == SelCategoriesDataObject.EXPAND_FLAG_PLUS){
                            if (obj.isExpanded())
                                collapseGroup(obj);
                            else
                                expandGroup(obj);

                        }

                    }
                });

                holder.btnBuy.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //if (obj.getProductid().length() > 0)
                        SelectCategoryActivity parent = ((SelectCategoryActivity)context);
                        parent.purchaseCategory(position);
                    }
                });


                holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //selectCategory(obj, isChecked);

                        SelectCategoryActivity parent = ((SelectCategoryActivity)context);

                        if (isChecked == true && parent.selectedCategories.size() >= 5) {
                            buttonView.setChecked(false);
                        }else {
                            int flag = (isChecked) ? SelCategoriesDataObject.ACTION_SELECT : SelCategoriesDataObject.ACTION_UNSELECT;


                            for (SelCategoriesDataObject cat : items) {
                                if (cat.getCatid() == null)
                                    continue;
                                if (cat.equals(obj))
                                    continue;
                                else if(cat.getCatid().equalsIgnoreCase(obj.getCatid())) {
                                    cat.setAction(flag);
                                }
                            }

                            obj.setAction(flag);

                            if (flag == SelCategoriesDataObject.ACTION_SELECT)
                                parent.selectedCategories.add(obj);
                            else{
                                for (SelCategoriesDataObject c: parent.selectedCategories){
                                    if(c.getCatid().equalsIgnoreCase(obj.getCatid())) {
                                        parent.selectedCategories.remove(c);
                                        break;
                                    }
                                }
                            }

                            parent.updateSelCatListView();

                            notifyDataSetChanged();
                        }
                    }
                });

                break;
            case TYPE_SEPERATOR:
                if (obj.getSecion() == SelCategoriesDataObject.SECTION_FAVOR)
                    holder.txtSectionTitle.setText("FAVORITES");
                else if (obj.getSecion() == SelCategoriesDataObject.SECTION_CATEGORY)
                    holder.txtSectionTitle.setText("CATEGORIES");
                else
                    holder.txtSectionTitle.setText("CATEGORY PACKAGES");
                break;
        }

        if (obj.isHidden())
            convertView.setVisibility(View.GONE);
        else
            convertView.setVisibility(View.VISIBLE);

        return convertView;
    }

    public void removeSelectedCategory(SelCategoriesDataObject cat){

        for (SelCategoriesDataObject c : items) {
            if (c.getCatid() == null)
                continue;
            else if(cat.getCatid().equalsIgnoreCase(c.getCatid())) {
                c.setAction(SelCategoriesDataObject.ACTION_UNSELECT);
            }
        }

        notifyDataSetChanged();
    }
    private void expandGroup(SelCategoriesDataObject group){

        collapsedGroupIds.remove(group.getGroupid());
        int index = items.indexOf(group);
        for(SelCategoriesDataObject cat: groupCategories){
            if (cat == group)
                continue;
            if (cat.getGroupid().equalsIgnoreCase(group.getGroupid())){
                index++;
                items.add(index, cat);
            }
        }

        group.setExpanded(true);

        notifyDataSetChanged();
    }
    private void collapseGroup(SelCategoriesDataObject group){

        collapsedGroupIds.add(group.getGroupid());
        for(SelCategoriesDataObject cat: groupCategories){
            if (cat == group)
                continue;
            if (cat.getGroupid().equalsIgnoreCase(group.getGroupid())){
                items.remove(cat);
            }
        }

        group.setExpanded(false);

        notifyDataSetChanged();
    }

    private void setCatFavorite(final SelCategoriesDataObject cat) {

        final int flag;
        if (cat.getFavor() == SelCategoriesDataObject.FAVOR_YES)
            flag = SelCategoriesDataObject.FAVOR_NO;
        else
            flag = SelCategoriesDataObject.FAVOR_YES;

        SaveCatFavorite asyncTask = new SaveCatFavorite(context, cat.getCatid(), flag,
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete() {

                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onLoadingComplete(Object obj) {
                        if (obj.equals("1")){

                            if (cat.getSecion() == SelCategoriesDataObject.SECTION_FAVOR){
                                favorCategories.remove(cat);
                                items.remove(cat);

                                for (SelCategoriesDataObject c : items){
                                    if (c.getCatid() == null)
                                        continue;
                                    if (c.getCatid().equalsIgnoreCase(cat.getCatid())){
                                        c.setFavor(flag);
                                        break;
                                    }
                                }
                            }else{

                                cat.setFavor(flag);

                                if (flag == SelCategoriesDataObject.FAVOR_YES){
                                    SelCategoriesDataObject c = new SelCategoriesDataObject(cat);
                                    c.setSection(SelCategoriesDataObject.SECTION_FAVOR);
                                    items.add(favorCategories.size()+1, c);
                                    favorCategories.add(c);
                                }else{
                                    for (SelCategoriesDataObject c : favorCategories){

                                        if (c.getCatid().equalsIgnoreCase(cat.getCatid())){
                                            items.remove(c);
                                            favorCategories.remove(c);

                                            break;
                                        }
                                    }
                                }

                            }

                            updateSectionHeader();
                            notifyDataSetChanged();

                        }else{

                        }
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });

        asyncTask.execute();
    }

    private void updateSectionHeader(){
        sectionHeader.clear();

        sectionHeader.add(0);
        sectionHeader.add(favorCategories.size()+1);
        sectionHeader.add(favorCategories.size()+1 + categories.size() + 1);
    }

    static class Holder {
    	ImageView ivCatImage;
        TextView txtGroupTitle;
        TextView txtSectionTitle;
        TextView txtExpand;
        ImageButton btnFavorite;
        TextView txtFavorPrice;
        ImageButton btnBuy;
        CheckBox cbSelect;
        ImageButton btnDetails;

        LinearLayout titleLayout;
        RelativeLayout favorLayout;
        RelativeLayout actionLayout;
        RelativeLayout disclosureLayout;
    }
}
