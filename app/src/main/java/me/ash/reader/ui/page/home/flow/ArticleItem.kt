package me.ash.reader.ui.page.home.flow

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import me.ash.reader.infrastructure.preference.LocalArticleListSwipeEndAction
import me.ash.reader.infrastructure.preference.LocalArticleListSwipeStartAction
import me.ash.reader.infrastructure.preference.LocalFlowArticleListDesc
import me.ash.reader.infrastructure.preference.LocalFlowArticleListFeedIcon
import me.ash.reader.infrastructure.preference.LocalFlowArticleListFeedName
import me.ash.reader.infrastructure.preference.LocalFlowArticleListImage
import me.ash.reader.infrastructure.preference.LocalFlowArticleListTime
import me.ash.reader.infrastructure.preference.LocalFlowArticleReadIndicator
import me.ash.reader.ui.component.FeedIcon
import me.ash.reader.ui.component.RYAsyncImage
import me.ash.reader.ui.component.SwipeableActionsBox
import me.ash.reader.ui.page.home.ArticleWithFeed
import me.ash.reader.ui.page.home.reading.ArticleListReadIndicator
import me.ash.reader.ui.theme.Shape20
import me.ash.reader.ui.theme.surfaceColorAtElevation

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ArticleItem(
    article: ArticleWithFeed,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    isShowFeedIcon: Boolean = LocalFlowArticleListFeedIcon.current,
    isShowFeedName: Boolean = LocalFlowArticleListFeedName.current,
    isShowArticleImage: Boolean = LocalFlowArticleListImage.current,
    isShowArticleDesc: Boolean = LocalFlowArticleListDesc.current,
    isShowArticleTime: Boolean = LocalFlowArticleListTime.current,
    articleListReadIndicator: ArticleListReadIndicator = LocalFlowArticleReadIndicator.current,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val swipeStartAction by LocalArticleListSwipeStartAction.current
    val swipeEndAction by LocalArticleListSwipeEndAction.current

    val isUnread = article.article.isUnread
    val isStarred = article.article.isStarred

    val alpha by animateColorAsState(
        targetValue = when (articleListReadIndicator) {
            ArticleListReadIndicator.AllRead -> if (isUnread) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.4f
            )
            ArticleListReadIndicator.ExcludingStarred -> if (isUnread || isStarred) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.4f
            )
            ArticleListReadIndicator.None -> MaterialTheme.colorScheme.onSurface
        },
        label = "alpha",
    )

    SwipeActionBox(
        isStarred = isStarred,
        isUnread = isUnread,
        swipeStartAction = swipeStartAction,
        swipeEndAction = swipeEndAction,
        onToggleStarred = { article.article.isStarred = !isStarred },
        onToggleRead = { article.article.isUnread = !isUnread },
    ) {
        val surfaceColor = MaterialTheme.colorScheme.surface
        val surfaceColorElevation1 = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        val surfaceColorElevation2 = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        onLongClick()
                    },
                )
                .padding(horizontal = 12.dp, vertical = 4.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (isShowFeedName) {
                    Text(
                        text = article.feed.name,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                    )
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (isStarred) {
                        Icon(
                            imageVector = Icons.Outlined.StarBorder,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.outline,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    if (isShowArticleTime) {
                        Text(
                            text = article.article.formattedDate,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline,
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
            ) {
                if (isShowFeedIcon) {
                    FeedIcon(
                        feed = article.feed,
                        placeholder = Icons.Outlined.StarBorder,
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                ) {
                    Text(
                        text = article.article.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = alpha,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (isShowArticleDesc && !article.article.shortDescription.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = article.article.shortDescription ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
            if (isShowArticleImage && !article.article.imgUrl.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                RYAsyncImage(
                    data = article.article.imgUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(top = 4.dp)
                        .clip(Shape20),
                )
            }
        }
    }
}

@Composable
private fun SwipeActionBox(
    isStarred: Boolean,
    isUnread: Boolean,
    swipeStartAction: SwipeAction,
    swipeEndAction: SwipeAction,
    onToggleStarred: () -> Unit,
    onToggleRead: () -> Unit,
    content: @Composable () -> Unit,
) {
    SwipeableActionsBox(
        startActions = listOf(
            SwipeAction(
                icon = swipeActionIcon(swipeStartAction),
                background = MaterialTheme.colorScheme.tertiaryContainer,
                onSwipe = {
                    when (swipeStartAction) {
                        SwipeAction.ToggleRead -> onToggleRead()
                        SwipeAction.ToggleStarred -> onToggleStarred()
                    }
                },
                label = swipeActionText(swipeStartAction, isUnread, isStarred),
            ),
        ),
        endActions = listOf(
            SwipeAction(
                icon = swipeActionIcon(swipeEndAction),
                background = MaterialTheme.colorScheme.secondaryContainer,
                onSwipe = {
                    when (swipeEndAction) {
                        SwipeAction.ToggleRead -> onToggleRead()
                        SwipeAction.ToggleStarred -> onToggleStarred()
                    }
                },
                label = swipeActionText(swipeEndAction, isUnread, isStarred),
            ),
        ),
    ) {
        content()
    }
}

@Composable
private fun swipeActionIcon(action: SwipeAction) = when (action) {
    SwipeAction.ToggleRead -> Icons.Outlined.CheckCircle
    SwipeAction.ToggleStarred -> Icons.Outlined.StarBorder
}

@Composable
private fun swipeActionText(action: SwipeAction, isUnread: Boolean, isStarred: Boolean) = when (action) {
    SwipeAction.ToggleRead -> if (isUnread) "Mark as read" else "Mark as unread"
    SwipeAction.ToggleStarred -> if (isStarred) "Unstar" else "Star"
}

enum class SwipeAction {
    ToggleRead,
    ToggleStarred,
}

@Composable
fun ArticleItemMenuContent(
    isRead: Boolean = false,
    isStarred: Boolean = false,
    onToggleRead: () -> Unit = {},
    onToggleStarred: () -> Unit = {},
    onMarkAboveAsRead: () -> Unit = {},
    onMarkBelowAsRead: () -> Unit = {},
    onShare: () -> Unit = {},
) {
    DropdownMenuItem(
        text = { Text(if (isRead) "Mark as unread" else "Mark as read") },
        onClick = onToggleRead,
    )
    DropdownMenuItem(
        text = { Text(if (isStarred) "Unstar" else "Star") },
        onClick = onToggleStarred,
    )
    HorizontalDivider()
    DropdownMenuItem(
        text = { Text("Mark above as read") },
        onClick = onMarkAboveAsRead,
    )
    DropdownMenuItem(
        text = { Text("Mark below as read") },
        onClick = onMarkBelowAsRead,
    )
    HorizontalDivider()
    DropdownMenuItem(
        text = { Text("Share") },
        onClick = onShare,
    )
}

private fun Modifier.clip(shape: androidx.compose.ui.graphics.Shape) = this.then(
    Modifier.clip(shape)
)