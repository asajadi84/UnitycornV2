package ir.unitycorn.app.adapters.recyclerviewadapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jay.widget.StickyHeaders;

import java.util.List;

import io.github.kbiakov.codeview.CodeView;
import io.github.kbiakov.codeview.adapters.Options;
import io.github.kbiakov.codeview.highlight.ColorTheme;
import ir.unitycorn.app.R;
import ir.unitycorn.app.activities.ViewImageActivity;

public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StickyHeaders {

    private Context context;
    private List<String> contentValue;
    private List<Integer> contentType;
    private SharedPreferences settingPrefs;

    //constructor
    public ArticleRecyclerViewAdapter(Context c, SharedPreferences s, List<String> contents, List<Integer> contentsT){
        this.context = c;
        this.contentValue = contents;
        this.contentType = contentsT;
        this.settingPrefs = s;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch(viewType){
            case 1:
                View view1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.article_recycler_item_title, parent, false);
                RecyclerView.ViewHolder viewHolder1 = new TitleViewHolder(view1);
                return viewHolder1;

            case 2:
                View view2 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.article_recycler_item_sub_title, parent, false);
                RecyclerView.ViewHolder viewHolder2 = new SubTitleViewHolder(view2);
                return viewHolder2;

            case 3:
                View view3 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.article_recycler_item_paragraph, parent, false);
                RecyclerView.ViewHolder viewHolder3 = new ParagraphViewHolder(view3);
                return viewHolder3;

            case 4:
                View view4 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.article_recycler_item_quotation, parent, false);
                RecyclerView.ViewHolder viewHolder4 = new QuotationViewHolder(view4);
                return viewHolder4;

            case 5:
                View view5 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.article_recycler_item_tip, parent, false);
                RecyclerView.ViewHolder viewHolder5 = new TipViewHolder(view5);
                return viewHolder5;

            case 6:
                View view6 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.article_recycler_item_bullet_paragraph, parent, false);
                RecyclerView.ViewHolder viewHolder6 = new BulletParagraphViewHolder(view6);
                return viewHolder6;

            case 7:
                View view7 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.article_recycler_item_description, parent, false);
                RecyclerView.ViewHolder viewHolder7 = new DescriptionViewHolder(view7);
                return viewHolder7;

            case 8:
                View view8 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.article_recycler_item_image, parent, false);
                RecyclerView.ViewHolder viewHolder8 = new ImageViewHolder(view8);
                return viewHolder8;

            case 9:
                View view9 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.article_recycler_item_code, parent, false);
                RecyclerView.ViewHolder viewHolder9 = new CodeViewHolder(view9);
                return viewHolder9;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){

            case 1:
                TitleViewHolder v1 = (TitleViewHolder) holder;

                v1.ARITitleTextView.setText(contentValue.get(position));
                v1.ARITitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                        settingPrefs.getInt("font_size", 16)+8
                );

//                Typeface iranSansBoldv1 = ResourcesCompat.getFont(context, R.font.iransansb);
//                v1.ARITitleTextView.setTypeface(iranSansBoldv1);

                if(settingPrefs.getBoolean("dark_mode",false)){
                    v1.ARITitleContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccentDark));
                    v1.ARITitleTextView.setTextColor(ContextCompat.getColor(context, android.R.color.white));
                }

                break;

            case 2:
                SubTitleViewHolder v2 = (SubTitleViewHolder) holder;

                v2.ARISubTitleTextView.setText(contentValue.get(position));
                v2.ARISubTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                        settingPrefs.getInt("font_size", 16)+8
                );

                Typeface iranSansBoldv2 = ResourcesCompat.getFont(context, R.font.iransansb);
                v2.ARISubTitleTextView.setTypeface(iranSansBoldv2);

                if(settingPrefs.getBoolean("dark_mode",false)){
                    v2.ARISubTitleTextView.setTextColor(ContextCompat.getColor(context, R.color.colorAccentDark));
                    v2.ARISubTitleContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                }

                break;

            case 3:
                ParagraphViewHolder v3 = (ParagraphViewHolder) holder;

                v3.ARIParagraphTextView.setText(contentValue.get(position));
                v3.ARIParagraphTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                        settingPrefs.getInt("font_size", 16)
                );

                if(settingPrefs.getBoolean("dark_mode",false)){
                    v3.ARIParagraphContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    v3.ARIParagraphTextView.setTextColor(ContextCompat.getColor(context, R.color.normalTextColorDark));
                }

                break;

            case 4:
                QuotationViewHolder v4 = (QuotationViewHolder) holder;

                Typeface iranSansBoldv4 = ResourcesCompat.getFont(context, R.font.iransansb);
                v4.ARIQuotationTextView.setTypeface(iranSansBoldv4);

                v4.ARIQuotationTextView.setText(contentValue.get(position));
                v4.ARIQuotationTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                        settingPrefs.getInt("font_size", 16)+4
                );

                if(settingPrefs.getBoolean("dark_mode",false)){
                    v4.ARIQuotationContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    v4.ARIQuotationTextView.setTextColor(ContextCompat.getColor(context, R.color.normalTextColorDark));
                }

                break;

            case 5:
                TipViewHolder v5 = (TipViewHolder) holder;

                v5.ARITipTextView.setText(contentValue.get(position));
                v5.ARITipTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                        settingPrefs.getInt("font_size", 16)
                );
                v5.ARITipBadgeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                        settingPrefs.getInt("font_size", 16)
                );

                if(settingPrefs.getBoolean("dark_mode",false)){
                    v5.ARITipContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    v5.ARITipTextView.setTextColor(ContextCompat.getColor(context, R.color.normalTextColorDark));
                    v5.ARITipBadgeTextView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccentDark));
                    v5.ARITipBadgeTextView.setTextColor(ContextCompat.getColor(context, android.R.color.white));
                }

                break;

            case 6:
                BulletParagraphViewHolder v6 = (BulletParagraphViewHolder) holder;

                v6.ARIBulletTextView.setText(contentValue.get(position));
                v6.ARIBulletTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                        settingPrefs.getInt("font_size", 16)
                );

                if(settingPrefs.getBoolean("dark_mode",false)){
                    v6.ARIBulletParagraphContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    v6.ARIBulletTextView.setTextColor(ContextCompat.getColor(context, R.color.normalTextColorDark));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        v6.ARIBulletImageView.setImageTintList(ContextCompat.getColorStateList(context, R.color.colorAccentDark));
                    }
                }

                break;

            case 7:
                DescriptionViewHolder v7 = (DescriptionViewHolder) holder;

                v7.ARIDescriptionTextView.setText(contentValue.get(position));
                v7.ARIDescriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                        settingPrefs.getInt("font_size", 16)-2
                );

                Typeface iranSansv7 = ResourcesCompat.getFont(context, R.font.iransans);
                //v7.ARIDescriptionTextView.setTypeface(iranSansv7, Typeface.ITALIC);

                if(settingPrefs.getBoolean("dark_mode",false)){
                    v7.ARIDescriptionContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                    v7.ARIDescriptionTextView.setTextColor(ContextCompat.getColor(context, R.color.normalSubTextColorDark));
                }

                break;

            case 8:
                ImageViewHolder v8 = (ImageViewHolder) holder;

                v8.ARIImageImageView.setImageResource(
                        context.getResources().getIdentifier(
                                contentValue.get(position),
                                "drawable",
                                context.getPackageName()
                        )
                );
                final int finalPosition = position;
                v8.ARIImageImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent imageViewIntent = new Intent(context, ViewImageActivity.class);
                        imageViewIntent.putExtra("imageId", contentValue.get(finalPosition));
                        context.startActivity(imageViewIntent);
                    }
                });

                if(settingPrefs.getBoolean("dark_mode",false)){
                    v8.ARIImageContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                }

                break;

            case 9:
                CodeViewHolder v9 = (CodeViewHolder) holder;

                //stringBuilderToString = stringBuilderToString.replaceAll("\\\\n", "\n");
                String codeFromDb = contentValue.get(position);
                String codeWithCorrectReturns = codeFromDb.replaceAll("\\\\n", "\n");

                if(settingPrefs.getBoolean("dark_mode", false)){
                    v9.ARICodeCodeView.setOptions(Options.Default.get(context)
                            .withLanguage("cs")
                            .withCode(codeWithCorrectReturns)
                            .withTheme(ColorTheme.MONOKAI));
                }else{
                    v9.ARICodeCodeView.setOptions(Options.Default.get(context)
                            .withLanguage("cs")
                            .withCode(codeWithCorrectReturns));
                }



                if(settingPrefs.getBoolean("dark_mode",false)){
                    v9.ARICodeContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                }

                break;
        }

    }

    @Override
    public int getItemCount() {
        return contentValue.size();
    }

    @Override
    public boolean isStickyHeader(int i) {
        if(contentType.get(i)==1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int getItemViewType(int position){
        return contentType.get(position);
    }

    //1 title
    public class TitleViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ARITitleContainer;
        TextView ARITitleTextView;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            ARITitleContainer = itemView.findViewById(R.id.ari_title_container);
            ARITitleTextView = itemView.findViewById(R.id.ari_title_textview);
        }
    }

    //2 sub title
    public class SubTitleViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ARISubTitleContainer;
        TextView ARISubTitleTextView;

        public SubTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            ARISubTitleContainer = itemView.findViewById(R.id.ari_sub_title_container);
            ARISubTitleTextView = itemView.findViewById(R.id.ari_sub_title_textview);
        }
    }

    //3 paragraph
    public class ParagraphViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ARIParagraphContainer;
        TextView ARIParagraphTextView;

        public ParagraphViewHolder(@NonNull View itemView) {
            super(itemView);
            ARIParagraphContainer = itemView.findViewById(R.id.ari_paragraph_container);
            ARIParagraphTextView = itemView.findViewById(R.id.ari_paragraph_textview);
        }
    }

    //4 quotation
    public class QuotationViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ARIQuotationContainer;
        TextView ARIQuotationTextView;

        public QuotationViewHolder(@NonNull View itemView) {
            super(itemView);
            ARIQuotationContainer = itemView.findViewById(R.id.ari_quotation_container);
            ARIQuotationTextView = itemView.findViewById(R.id.ari_quotation_textview);
        }
    }

    //5 tip
    public class TipViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ARITipContainer;
        TextView ARITipTextView;
        TextView ARITipBadgeTextView;

        public TipViewHolder(@NonNull View itemView) {
            super(itemView);
            ARITipContainer = itemView.findViewById(R.id.ari_tip_container);
            ARITipTextView = itemView.findViewById(R.id.ari_tip_textview);
            ARITipBadgeTextView = itemView.findViewById(R.id.ari_tip_badge_textview);
        }
    }

    //6 bullet paragraph
    public class BulletParagraphViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ARIBulletParagraphContainer;
        TextView ARIBulletTextView;
        ImageView ARIBulletImageView;

        public BulletParagraphViewHolder(@NonNull View itemView) {
            super(itemView);
            ARIBulletParagraphContainer = itemView.findViewById(R.id.ari_bullet_paragraph_container);
            ARIBulletTextView = itemView.findViewById(R.id.ari_bullet_paragraph_textview);
            ARIBulletImageView = itemView.findViewById(R.id.ari_bullet_paragraph_imageview);
        }
    }

    //7 description
    public class DescriptionViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ARIDescriptionContainer;
        TextView ARIDescriptionTextView;

        public DescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            ARIDescriptionContainer = itemView.findViewById(R.id.ari_description_container);
            ARIDescriptionTextView = itemView.findViewById(R.id.ari_description_textview);
        }
    }

    //8 image
    public class ImageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ARIImageContainer;
        ImageView ARIImageImageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ARIImageContainer = itemView.findViewById(R.id.ari_image_container);
            ARIImageImageView = itemView.findViewById(R.id.ari_image_imageview);
        }
    }

    //9 code
    public class CodeViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ARICodeContainer;
        CodeView ARICodeCodeView;

        public CodeViewHolder(@NonNull View itemView) {
            super(itemView);
            ARICodeContainer = itemView.findViewById(R.id.ari_code_container);
            ARICodeCodeView = itemView.findViewById(R.id.ari_code_codeview);
        }
    }
}
