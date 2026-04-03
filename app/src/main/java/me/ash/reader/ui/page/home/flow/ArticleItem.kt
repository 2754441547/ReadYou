package me.ash.reader.ui.page.home.flow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import me.ash.reader.infrastructure.preference.*
import me.ash.reader.ui.theme.LocalThemeColors
import me.ash.reader.ui.component.FeedIcon
import me.ash.reader.ui.page.home.ArticleWithFeed
import me.ash.reader.ui.page.home.reading.*

/**
 * 微信公众号风格 - 文章列表项
 * 布局：左侧头像 + 右侧（名称+时间）+ 标题+摘要 + 可选配图
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleItem(
    article: ArticleWithFeed,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    isShowFeedIcon: Boolean = LocalFlowArticleListFeedIcon.current,
    isShowFeedName: Boolean = LocalFlowArticleListFeedName.current,
    isShowArticleImage: Boolean = LocalFlowArticleListImage.current,
    isShowArticleDesc: Boolean = LocalFlowArticleListDesc.current,
    isShowArticleTime: Boolean = LocalFlowArticleListTime.current,
    articleListReadIndicator: ArticleListReadIndicator = LocalFlowArticleListReadIndicator.current,
) {
    val isUnread = article.article.isUnread
    val isStarred = article.article.isStarred
    
    // 已读状态透明度
    val alphaValue = when (articleListReadIndicator) {
        ArticleListReadIndicator.AllRead -> if (isUnread) 1f else 0.5f
        ArticleListReadIndicator.ExcludingStarred -> if (isUnread || isStarred) 1f else 0.5f
        ArticleListReadIndicator.None -> 1f
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .alpha(alphaValue)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // 左侧：公众号头像
            if (isShowFeedIcon) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    FeedIcon(
                        feed = article.feed,
                        size = 40.dp,
                        placeholder = Icons.Outlined.StarBorder
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
            }
            
            // 右侧内容区域
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 顶部：公众号名称 + 时间
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 公众号名称
                    if (isShowFeedName) {
                        Text(
                            text = article.feed.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    // 时间
                    if (isShowArticleTime) {
                        Text(
                            text = article.article.formattedDate,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            fontSize = 11.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                // 标题
                Text(
                    text = article.article.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 22.sp
                )
                
                // 摘要
                if (isShowArticleDesc && !article.article.shortDescription.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = article.article.shortDescription ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp
                    )
                }
                
                // 配图（右侧小图风格）
                if (isShowArticleImage && !article.article.imgUrl.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            model = article.article.imgUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                
                // 底部：星标图标
                if (isStarred) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Icon(
                        imageVector = Icons.Outlined.StarBorder,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

// 已读指示器枚举
enum class ArticleListReadIndicator {
    None,
    AllRead,
    ExcludingStarred
}