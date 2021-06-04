package ir.unitycorn.app;

import java.util.List;

public class ArticleTableModel {
    public final int articleTableCategory;
    public final String articleTableTitle;
    public final int articleTableUnreadBadge;
    public final Boolean articleTableIsPremium;
    public final List<String> articleTableListNames;
    public final List<String> articleTableListIds;
    public final List<Boolean> articleTableListUnreadStatus;

    public ArticleTableModel(int articleTableCategory, String articleTableTitle, int articleTableUnreadBadge, Boolean articleTableIsPremium, List<String> articleTableListNames, List<String> articleTableListIds, List<Boolean> articleTableListUnreadStatus) {
        this.articleTableCategory = articleTableCategory;
        this.articleTableTitle = articleTableTitle;
        this.articleTableUnreadBadge = articleTableUnreadBadge;
        this.articleTableIsPremium = articleTableIsPremium;
        this.articleTableListNames = articleTableListNames;
        this.articleTableListIds = articleTableListIds;
        this.articleTableListUnreadStatus = articleTableListUnreadStatus;
    }
}

