
package com.igames2go.t4f.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.igames2go.t4f.Application.T4FApplication;
import com.igames2go.t4f.Activities.HomeScreen;
import com.igames2go.t4f.R;
import com.igames2go.t4f.utils.FacebookManager;
import com.igames2go.t4f.utils.Utils;
import com.igames2go.t4f.view.SlideMenu.SlideMenuAdapter.MenuDesc;

import java.util.ArrayList;
import java.util.List;

public class SlideMenu {
//just a simple adapter
	public static class SlideMenuAdapter extends ArrayAdapter<SlideMenu.SlideMenuAdapter.MenuDesc> {
	    Activity act;
	    List<MenuDesc> items;
	    
	    class MenuItem {
	        public TextView label;
	        public ImageView icon;
	    }
	    static class MenuDesc {
	        public int icon;
	        public String label;
	    }
	    public SlideMenuAdapter(Activity act, List<MenuDesc> items) {
	        super(act, R.layout.menu_listitem, items);
	        this.act = act;
	        this.items = items;
	        }
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View rowView = convertView;
	        if (rowView == null) {
	            LayoutInflater inflater = act.getLayoutInflater();
	            rowView = inflater.inflate(R.layout.menu_listitem, null);
	            MenuItem viewHolder = new MenuItem();
	            viewHolder.label = (TextView) rowView.findViewById(R.id.menu_label);
	            viewHolder.icon = (ImageView) rowView.findViewById(R.id.menu_icon);
	            rowView.setTag(viewHolder);
	        }
	
	        MenuItem holder = (MenuItem) rowView.getTag();
	        String s = items.get(position).label;
	        holder.label.setText(s);
	        holder.icon.setImageResource(items.get(position).icon);
	
	        return rowView;
	    }
	}

	private static boolean menuShown = false;
	private static View menu;
	private static LinearLayout content;
	private static FrameLayout parent;
	private static int menuSize;
	private static int statusHeight = 0;
	private Activity act;
	private String [] menuItemTitles;
    private String [] menuItemIcons;
    List<String> menuIconList;
    private T4FApplication mApplication;
    
	public SlideMenu(Activity act) {
	    this.act = act;
	    mApplication = (T4FApplication)act.getApplication();
	}
	//call this in your onCreate() for screen rotation
	public void checkEnabled() {
	    if(menuShown)
	        this.show(false);
	}
	public boolean isShown(){
		return menuShown;
	}
	public void show() {
	//get the height of the status bar
	    if(statusHeight == 0) {
	        Rect rectgle = new Rect();
	        Window window = act.getWindow();
	        window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
	        statusHeight = rectgle.top;
	        }
	    this.show(true);
	}
	@SuppressLint("NewApi")
	public void show(boolean animate) {
		
		menuSize = Utils.dpToPx(280, act);
		if ((act.getResources().getConfiguration().screenLayout & 
			    Configuration.SCREENLAYOUT_SIZE_MASK) == 
			        Configuration.SCREENLAYOUT_SIZE_XLARGE) {
			menuSize = Utils.dpToPx(560, act);
		}
		
		Log.d("Menu Width", menuSize + "");
	    content = ((LinearLayout) act.findViewById(R.id.layput_container));
	    parent = (FrameLayout) content.getParent();
	    
	    FrameLayout.LayoutParams parm = (FrameLayout.LayoutParams) content.getLayoutParams();
	    parm.setMargins(menuSize, 0, -menuSize, 0);
	    content.setLayoutParams(parm);
	//animation for smooth slide-out
	    TranslateAnimation ta = new TranslateAnimation(-menuSize, 0, 0, 0);
	    ta.setDuration(300);
	    if(animate)
	        content.startAnimation(ta);
	    
	    LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    menu = inflater.inflate(R.layout.menu, null);
	    FrameLayout.LayoutParams lays = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.LEFT);
	    lays.setMargins(0,0, 0, 0);
	    menu.setLayoutParams(lays);
	    parent.addView(menu);
	    
	    TextView titleView = (TextView) act.findViewById(R.id.menu_title);
	    titleView.setText(act.getString(R.string.app_name));
	    
	    ListView list = (ListView) act.findViewById(R.id.menu_listview);
	    list.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            //handle your menu-click
	        	view.setSelected(true);
	        	SlideMenu.this.hide(400);
	        	
	        	final int p = position; 
	        	final Handler handler = new Handler();
	        	handler.postDelayed(new Runnable() {
	        	  @Override
	        	  public void run() {
	        		  String menuName = menuIconList.get(p);
	  	        	
	  	        	if (menuName.equalsIgnoreCase("menu_games")) {
	  	                
	  	            }else if (menuName.equalsIgnoreCase("menu_refresh")) {
	  	                ((HomeScreen)act).refreshButtonPressed();
	  	            }else if (menuName.equalsIgnoreCase("menu_invite")) {
	  	                
	  	            	((HomeScreen)act).inviteButtonPressed();
	  	                
	  	            }else if (menuName.equalsIgnoreCase("menu_fb_home_login") || menuName.equalsIgnoreCase("menu_fb_home_logout") ){
	  	                
	  	            	((HomeScreen)act).fbButtonPressed();
	  	                
	  	            }else if (menuName.equalsIgnoreCase("menu_statistics")) {
	  	                
	  	            	((HomeScreen)act).statsButtonPressed();
	  	                
	  	            }else if (menuName.equalsIgnoreCase("menu_leaderboard")) {
	  	            	((HomeScreen)act).leaderboardButtonPressed();
	  	                
	  	            }else if (menuName.equalsIgnoreCase("menu_store")) {
	  	                
	  	                
	  	            }else if (menuName.equalsIgnoreCase("menu_profile")) {
	  	                
	  	            	((HomeScreen)act).profileButtonPressed();
	  	                
	  	            }else if (menuName.equalsIgnoreCase("menu_settings")) {
	  	                
	  	            	((HomeScreen)act).settingButtonPressed();
	  	                
	  	            }else if (menuName.equalsIgnoreCase("menu_help")) {
	  	                
	  	            	((HomeScreen)act).helpButtonPressed();
	  	                
	  	            }else if (menuName.equalsIgnoreCase("menu_support")) {
	  	            	((HomeScreen)act).supportButtonPressed();

	  	            }else if (menuName.equalsIgnoreCase("menu_rate")) {
						((HomeScreen)act).rateButtonPressed();

					}else if (menuName.equalsIgnoreCase("menu_buy_ad")) {
						((HomeScreen)act).buyAdButtonPressed();

					}else if (menuName.equalsIgnoreCase("menu_about")) {
	  	            	((HomeScreen)act).aboutButtonPressed();
	  	                
	  	            }else if (menuName.equalsIgnoreCase("menu_logout")) {
	  	                ((HomeScreen)act).logoutButtonPressed();
	  	            }
	        	  }
	        	}, 400);
	
	        }
	    });
	    if(animate)
	        menu.startAnimation(ta);
	    menu.findViewById(R.id.overlay).setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	            SlideMenu.this.hide();
	        }
	    });
	    //Utils.enableDisableViewGroup((LinearLayout) parent.findViewById(android.R.id.content).getParent(), false);
	    //((ExtendedViewPager) act.findViewById(R.id.viewpager)).setPagingEnabled(false);
	    //((ExtendedPagerTabStrip) act.findViewById(R.id.viewpager_tabs)).setNavEnabled(false);
	    menuShown = true;
	    this.fill();
	}
	public void fill() {
	    ListView list = (ListView) act.findViewById(R.id.menu_listview);
	    
	    menuItemTitles = act.getResources().getStringArray(R.array.menu_item_titles);
	    menuItemIcons = act.getResources().getStringArray(R.array.menu_item_icons);
	    
	    menuIconList = new ArrayList<String>(); 
	    
	    //SlideMenuAdapter.MenuDesc[] items = new SlideMenuAdapter.MenuDesc[menuCount];
	    
	    List<MenuDesc> menuList = new ArrayList<MenuDesc>();
	    //fill the menu-items here
	    int j=0;
	    for(int i=0; i<menuItemTitles.length; i++){
	    	
	    	if(mApplication.getPlay_mode() == T4FApplication.PLAY_MODE_NO_LOGIN){
	    		if(menuItemIcons[i].equalsIgnoreCase("menu_fb_home_login")){
	    			continue;
	    		}
	    		if(menuItemIcons[i].equalsIgnoreCase("menu_invite")){
	    			continue;
	    		}
	    		if(menuItemIcons[i].equalsIgnoreCase("menu_profile")){
	    			continue;
	    		}
	    		if(menuItemIcons[i].equalsIgnoreCase("menu_logout")){
	    			menuItemTitles[i] = "Back to Login Options";
	    		}
	    		if(menuItemIcons[i].equalsIgnoreCase("menu_leaderboard")){
		    	    continue;
		    	}
		    	if(menuItemIcons[i].equalsIgnoreCase("menu_store")){
		    	    continue;
		    	}
	    	}else{
	    		
	    		if(menuItemIcons[i].equalsIgnoreCase("menu_leaderboard")){
		    		if(T4FApplication.leaderboardHide)
				    	continue;
		    	}
		    	if(menuItemIcons[i].equalsIgnoreCase("menu_store")){
		    		if(T4FApplication.storeHide)
				    	continue;
		    	}
		    	
	    		if(FacebookManager.init()){
	    			if(menuItemIcons[i].equalsIgnoreCase("menu_fb_home_login")){
	    				//	menuItemIcons[i] = "menu_fb_home_logout";
	    				// 29 May 2015 larry Change so blue facebook image shown when not logged into facebook
	    				menuItemIcons[i] = "menu_fb_home_login";
		    		}
	    		}else{
	    			if(menuItemIcons[i].equalsIgnoreCase("menu_fb_home_logout")){
	    				// menuItemIcons[i] = "menu_fb_home_login";
	    				// 29 May 2015 larry Change so blue facebook image shown when not logged into facebook
	    				menuItemIcons[i] = "menu_fb_home_logout";
		    		}
	    			if(menuItemIcons[i].equalsIgnoreCase("menu_invite")){
		    			continue;
		    		}
	    		}
	    	}

			if(!act.getResources().getString(R.string.ad_enabled).equalsIgnoreCase("1")){
				if(menuItemIcons[i].equalsIgnoreCase("menu_buy_ad")){
					continue;
				}
			}
	    	menuIconList.add(menuItemIcons[i]);
	    	MenuDesc item = new SlideMenuAdapter.MenuDesc();
		    int resourceId = act.getResources().getIdentifier(menuItemIcons[i], "drawable", act.getPackageName());
		    item.icon = resourceId;
		    item.label = menuItemTitles[i];
		    menuList.add(item);
	    }
	    
	    SlideMenuAdapter adap = new SlideMenuAdapter(act, menuList);
	    list.setAdapter(adap);
	}
	public void hide(int duration){
		TranslateAnimation ta = new TranslateAnimation(0, -menuSize, 0, 0);
	    ta.setDuration(duration);
	    menu.startAnimation(ta);
	    parent.removeView(menu);
	
	    TranslateAnimation tra = new TranslateAnimation(menuSize, 0, 0, 0);
	    tra.setDuration(duration);
	    content.startAnimation(tra);
	    FrameLayout.LayoutParams parm = (FrameLayout.LayoutParams) content.getLayoutParams();
	    parm.setMargins(0, 0, 0, 0);
	    content.setLayoutParams(parm);
	    //Utils.enableDisableViewGroup((LinearLayout) parent.findViewById(android.R.id.content).getParent(), true);
	    //((ExtendedViewPager) act.findViewById(R.id.viewpager)).setPagingEnabled(true);
	    //((ExtendedPagerTabStrip) act.findViewById(R.id.viewpager_tabs)).setNavEnabled(true);
	    menuShown = false;
	}
	public void hide() {
	    hide(300);
	}
}
