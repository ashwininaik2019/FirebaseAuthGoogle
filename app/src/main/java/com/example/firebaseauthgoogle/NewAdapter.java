package com.example.firebaseauthgoogle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.ViewHolder> {
    private List<Upload>list_data,m_data;
    private Context ct,contxt,contxtnw;
    String mg,s_mg;

    Upload ld;

    private List<String> l_data;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    private SimpleExoPlayer player;
    private PlayerView playerView;
    public NewAdapter(Context ct,List<Upload> list_data) {
        this.ct = ct;
        this.list_data = list_data;

    }
    public NewAdapter(Context contxt,List<String> l_data,String mg) {
        this.contxt = contxt;
        this.l_data = l_data;
        this.mg = mg;

    }
    public NewAdapter(Context contxtnw,List<Upload> m_data,String mg,String s_mg) {
        this.contxtnw = contxtnw;
        this.m_data = m_data;
        this.mg = mg;
        this.s_mg = s_mg;
    }
    FirebaseFirestore db;
    public String tval;
    public String name_val,title_val,url_val;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(ct).inflate(R.layout.layout_images,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
try {

        /*db = FirebaseFirestore.getInstance();
        // Code here executes on main thread after user presses button
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                System.out.println("show data "+document.getData().get("Title"));

                                tval = document.getData().get("Title").toString();
                               *//*TextView txt = (TextView)itemView.findViewById(R.id.tvtitleval);
                                txt.setText((CharSequence) document.getData().get("Title"));*//*
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });*/

    ld = list_data.get(position);
   //Upload ld = l_data.get(position);
    //Upload s_val =m_data.get(position);
   //  name_val =ld.getName().toString().trim();


     if(ld.getName() != null && !ld.getName().equals("null") && ld.getTitle()!= null && !ld.getTitle().equals("null") && ld.getUrl()!= null && !ld.getUrl().equals("null"))
    {
        //name_val =ld.getName().toString();
       // url_val = ld.getUrl().toString();

      //  System.out.println("got name and url" + ld.getUrl().toString());
        holder.txt_title.setText(ld.getTitle());
        holder.txt_desc.setText(ld.getDesc());
        holder.txt_ctgry.setText(ld.getCategory());
        holder.tvname.setText(ld.getName());

        Glide.with(ct)
                .load(ld.getUrl())
                .into(holder.imageView);
        player = ExoPlayerFactory.newSimpleInstance(ct);
        playerView.setPlayer(player);
        /*Uri uri = Uri.parse(getString(R.string.media_url_mp4));*/
        Uri uri = Uri.parse(ld.getvideo_url().toString());
        MediaSource mediaSource = buildMediaSource(uri);
       // player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
    }
    else //if(name_val == null)
    {
        // title_val =ld.getTitle().toString();
        if(ld.getTitle()!= null && !ld.getTitle().equals("null"))
        {
            holder.txt_title.setText(ld.getTitle());
            //title_val =ld.getTitle().toString();
            System.out.println("got title" + ld.getTitle().toString());

        }
        else //if (title_val!=null)
        {
            //title_val =ld.getTitle().toString();
            System.out.println("No title" + ld.getTitle().toString());
        }
    }


/*    String url_val=ld.getUrl().toString();
    if(val=="null" && url_val=="null")
    {
        //System.out.println("uploaded title" + ld.getName().toString());
        String t_val =ld.getTitle().toString();
    }
    else
    {
        System.out.println("uploaded not null title" + ld.getName().toString());
    }
    String t_val =ld.getTitle().toString();
  //  Upload sdata=m_data.get(position);
     //   System.out.println("uploaded title" + l_data.get(position) + l_data.size()+ld.getTitle().toString()) ;
    if(val=="null"  && t_val!="null" && url_val=="null") {
        System.out.println("uploaded title" + ld.getTitle().toString());
        //   holder.txt.setText(ld.getTitle());
    }
    else if(ld.getName().toString() !=null && ld.getTitle().toString()=="null" && ld.getUrl().toString()!=null) {
        holder.tvname.setText(ld.getName());
        Glide.with(ct)
                .load(ld.getUrl())
                .into(holder.imageView);
    *//*holder.itemView.setOnClickListener(new View.OnClickListener() {  // <--- here
        @Override
        public void onClick(View v) {
            Log.i("W4K", "Click-" + position);
            ct.startActivity(new Intent(ct, UploadImageActivity.class));  // <--- here
        }
    });*//*
    }*/
}
catch(Exception e)
{
    System.out.println("Exception " + e.getMessage());
}
    }

    /*Picasso.placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()*/
    @Override
    public int getItemCount() {
        return list_data.size();

    }

    public int getItemCount1() {
        return m_data.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
      //  private PlayerView playerView;
        private TextView tvname,txt_title,txt_desc,txt_ctgry;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.imageView);
            tvname=(TextView)itemView.findViewById(R.id.textViewName);
            txt_title = (TextView)itemView.findViewById(R.id.tvtitleval);
            txt_desc = (TextView)itemView.findViewById(R.id.tvdescval);
            txt_ctgry = (TextView)itemView.findViewById(R.id.tvcatgryval);
            playerView =(PlayerView)itemView.findViewById(R.id.video_view);

        }
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(ct);
        playerView.setPlayer(player);
        /*Uri uri = Uri.parse(getString(R.string.media_url_mp4));*/
        Uri uri = Uri.parse(ld.getvideo_url().toString());
        MediaSource mediaSource = buildMediaSource(uri);
       // player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, false, false);
    }
    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(ct, "exoplayer-codelab");
        return new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
    }

    private void onStart() {
       // super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }
    }

    //@Override
    public void onResume() {
        //super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

   // @Override
    public void onPause() {
       // super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

   // @Override
    public void onStop() {
       // super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }
}
