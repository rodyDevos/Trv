package com.igames2go.t4f.Activities.SelectCategory;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.igames2go.t4f.Activities.GameOptionsActivity;
import com.igames2go.t4f.Activities.LoadingListener;
import com.igames2go.t4f.Activities.SelectCategory.SelectCategoryAdapter;
import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.R;
import com.igames2go.t4f.data.SelCategoriesDataObject;
import com.igames2go.t4f.tasks.GetCategoryList;
import com.igames2go.t4f.tasks.SavePurchase;
import com.igames2go.t4f.tasks.StartTheGame;
import com.igames2go.t4f.utils.ButtonClickListener;
import com.igames2go.t4f.utils.DialogUtil;
import com.igames2go.t4f.utils.ImageLoader;


import com.igames2go.t4f.utils.inappbilling.IabHelper;
import com.igames2go.t4f.utils.inappbilling.IabResult;
import com.igames2go.t4f.utils.inappbilling.Inventory;
import com.igames2go.t4f.utils.inappbilling.Purchase;

import java.util.ArrayList;

public class SelectCategoryActivity extends Activity {

    private Button btnCancel, btnNext;
    private SelectCategoryAdapter adapter;
    private LinearLayout selCatListLayout, selCatLayout;
    private ListView categoryListView;
    private TextView txtSelCatTitle;

    private final String TAG = "SelectCategory";
    private ImageLoader loader;

    private ArrayList<SelCategoriesDataObject> categories;
    public ArrayList<SelCategoriesDataObject> selectedCategories;
    private ArrayList<String> groupIds;

    private Integer currentPosition;
    private ArrayList<String> playerIds;
    private String message;

    private T4FApplication mApplication;
    IabHelper mHelper;

    String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqUpCsKJ7FOXIgc0E1vjk2YEJ19gGJCsEsC8bZkgq9fWhSL79VXVs/Htof1jdY1qht+5gNfSfBmNXoM31C0n0exXFh3ebrUbmEwUXqtf9I45LkGpzF6IUoYduveIaYBR0J08Ouc6KoyLeOZ9rHDFexd+GhmFKSVIfSdTsurPKq+aDWucxSlY+Xq/KISdS93p/xKZplyVfq1d8gMW2ACVf1pj2LSlpr9FoC5cCkFq1XyYkKWbnePcyLXR5S5EQ0IR9v6Lxlz+D897QXZ9aGZjDbgbdc613AJFD6ybKoWh0qwaZw0DEUahuASjpHLcWJBbndlmtSjwoDE885fuYdwJSpwIDAQAB";

    static final String ITEM_SKU = "android.test.purchased";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        playerIds = getIntent().getStringArrayListExtra("playerIds");
        message = getIntent().getStringExtra("message");

        mApplication = (T4FApplication) getApplication();

        loader = new ImageLoader(this);
        selectedCategories = new ArrayList<SelCategoriesDataObject>();
        selCatListLayout = (LinearLayout)findViewById(R.id.selCatListLayout);
        txtSelCatTitle = (TextView)findViewById(R.id.txtSelCategoryTitle);

        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnNext = (Button)findViewById(R.id.btnNext);

        categories = new ArrayList<SelCategoriesDataObject>();
        groupIds = new ArrayList<String>();
        adapter = new SelectCategoryAdapter(this, R.layout.layout_selectcategorylistadapter);

        categoryListView = (ListView)findViewById(R.id.categoryListView);
        categoryListView.setAdapter(adapter);

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result)
                                       {
                                           if (!result.isSuccess()) {
                                               Log.d(TAG, "In-app Billing setup failed: " +
                                                       result);
                                           } else {
                                               Log.d(TAG, "In-app Billing is set up OK");
                                           }
                                       }
                                   });

        updateSelCatListView();
        getCategoryList();
    }

    @Override
    public void onBackPressed() {
        cancel(new View(getApplicationContext()));
        return;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    public void updateSelCatListView(){

        selCatListLayout.removeAllViews();
        for (int i = 0; i < selectedCategories.size(); i++){
            selCatLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_selcategory, null);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0.2f);
            //param.setMargins(5, 0, 5, 0);
            selCatLayout.setLayoutParams(param);

            final SelCategoriesDataObject cat = selectedCategories.get(i);

            Holder holder = new Holder();
            holder.ivCatImage = (ImageView)selCatLayout.findViewById(R.id.ivCatImage);
            holder.txtCatTitle = (TextView) selCatLayout.findViewById(R.id.txtCatTitle);
            holder.btnRemove = (ImageButton) selCatLayout.findViewById(R.id.btnRemove);

            holder.txtCatTitle.setText(cat.getCatname());

            loader.DisplayImage(
                    getString(R.string.url_category) + cat.getImagesquare(),
                    holder.ivCatImage, false);

            holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedCategories.remove(cat);
                    adapter.removeSelectedCategory(cat);

                    updateSelCatListView();
                }
            });
            selCatListLayout.addView(selCatLayout);
        }

        if (selectedCategories.size() < 5)
            btnNext.setVisibility(View.INVISIBLE);
        else
            btnNext.setVisibility(View.VISIBLE);

        String title = "";

        switch (selectedCategories.size()){
            case 0:
                title = "Select 5 Categories for the New Game";
                break;
            case 1:
                title = "Select 4 More Categories";
                break;
            case 2:
                title = "Select 3 More Categories";
                break;
            case 3:
                title = "Select 2 More Categories";
                break;
            case 4:
                title = "Select 1 More Category";
                break;
            case 5:
                title = "5 Categories Selected";
                break;
        }

        txtSelCatTitle.setText(title);
    }

    public void purchaseCategory(Integer position){

        currentPosition = position;
        SelCategoriesDataObject cat = adapter.getItem(currentPosition);

        if (cat.getAction() == SelCategoriesDataObject.ACTION_BUY && cat.getProductid().length() > 0)
        mHelper.launchPurchaseFlow(this, cat.getProductid(), 10001,
                mPurchaseFinishedListener, "mypurchasetoken");
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();
            }

        }
    };

    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                // Handle failure
            } else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                        mConsumeFinishedListener);
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        setPurchase(purchase.getOrderId());

                    } else {
                        // handle error
                    }
                }
            };

    private void setPurchase(String orderId) {

        final SelCategoriesDataObject cat = adapter.getItem(currentPosition);
        SavePurchase asyncTask = new SavePurchase(this, cat.getProductid(), orderId,
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete() {

                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onLoadingComplete(Object obj) {
                        if (obj.equals("1")){


                            cat.setAction(SelCategoriesDataObject.ACTION_UNSELECT);

                            adapter.notifyDataSetChanged();

                        }else{

                        }
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });

        asyncTask.execute();
    }
    private void getCategoryList() {
        GetCategoryList asyncTask = new GetCategoryList(this,
                new LoadingListener() {

                    @Override
                    public void onLoadingComplete() {

                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onLoadingComplete(Object obj) {

                        categories = (ArrayList<SelCategoriesDataObject>) obj;

                        Log.e(TAG, "item size: " + categories.size());

                        boolean headerAdded = false;
                        int curSection = SelCategoriesDataObject.SECTION_FAVOR;

                        int prevSection = -1;
                        for(int i = 0; i< categories.size(); i++){
                            SelCategoriesDataObject category = categories.get(i);

                            if (prevSection == category.getSecion())
                                headerAdded = true;
                            else
                                headerAdded = false;

                            if (headerAdded){
                                adapter.addItem(category);
                            }else{
                                SelCategoriesDataObject header = new SelCategoriesDataObject();
                                header.setSection(curSection);
                                adapter.addSectionHeaderItem(header);
                                curSection++;

                                adapter.addItem(category);
                            }

                            prevSection = category.getSecion();

                            Log.d("Name", category.getCatname());
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Object error) {

                    }
                });

        asyncTask.execute();
    }

    static class Holder {
        ImageView ivCatImage;
        TextView txtCatTitle;
        ImageButton btnRemove;
    }

    private void doStartGame() {

        String categoryIds = "";
        for (SelCategoriesDataObject cat : selectedCategories){
            if (categoryIds.length() != 0)
                categoryIds += ",";
            categoryIds += cat.getCatid();
        }

        StartTheGame asyncTask = new StartTheGame(this, playerIds, categoryIds,
                new StartGameResult(), new LoadingListener() {

            @Override
            public void onLoadingComplete() {

            }

            @Override
            public void onLoadingComplete(Object obj) {
                StartGameResult result = (StartGameResult) obj;
                if (result != null && result.getGameId() != null
                        && result.getGamePlaID() != null) {
                    mApplication.resetPlayer();
                    Intent intent = new Intent(getApplicationContext(),
                            GameOptionsActivity.class);
                    intent.putExtra("game_id", result.getGameId() + "");
                    intent.putExtra("gameplaid", result.getGamePlaID()
                            + "");
                    startActivity(intent);
                    SelectCategoryActivity.this.finish();
                } else
                    Toast.makeText(SelectCategoryActivity.this,
                            "Start the Game has failed ",
                            Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Object error) {

            }
        });
        asyncTask.execute();
    }

    public class StartGameResult {
        String mGameId;
        String mGamePlaID;

        public String getGameId() {
            return mGameId;
        }

        public String getGamePlaID() {
            return mGamePlaID;
        }

        public void setGameId(String mGameId) {
            this.mGameId = new String(mGameId);
        }

        public void setGamePlaID(String mGamePlaID) {
            this.mGamePlaID = new String(mGamePlaID);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    Dialog d;
    public void cancel(View v){
        d = DialogUtil.createDialog_title_divider_message_two_btn(this, null,
                false, getString(R.string.cancel_dialog_msg_onnewgame), R.drawable.button_no,
                R.drawable.button_yes, new ButtonClickListener() {

                    @Override
                    public void onButtonClick(int s)
                            throws NullPointerException {
                        if (s == R.drawable.button_yes) {
                            mApplication.resetPlayer();
                            d.dismiss();
                            SelectCategoryActivity.this.finish();
                        } else
                            d.dismiss();
                    }
                }, -1);
        d.show();
    }

    public void next(View v){

        d = DialogUtil.createDialog_title_divider_message_two_btn(this,
                "Start Game?", true, message, R.drawable.button_cancel, R.drawable.button_ok,
                new ButtonClickListener() {

                    @Override
                    public void onButtonClick(int s)
                            throws NullPointerException {
                        if (s == R.drawable.button_ok) {
                            doStartGame();
                            d.dismiss();
                        }else{
                            d.dismiss();
                        }
                    }
                }, -1);
        d.show();
    }
}
