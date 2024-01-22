package com.umanji.umanjiapp.ui.fragment.post.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.compat.R.id.text
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter
import com.leocardz.link.preview.library.LinkPreviewCallback
import com.leocardz.link.preview.library.SourceContent
import com.leocardz.link.preview.library.TextCrawler
import com.umanji.umanjiapp.R
import com.umanji.umanjiapp.common.util.*
import com.umanji.umanjiapp.model.PostModel
import kotlinx.android.synthetic.main.item_post.view.*
import kotlinx.android.synthetic.main.link_preview.view.*


class GroupAdapter(
        private val dateUtils: DateUtils
) : HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, GroupAdapter.Header, PostModel, Nothing>() {

    @Suppress("PrivatePropertyName", "unused")
    private val TAG: String = GroupAdapter::class.java.simpleName

    var itemListener: ((item: PostModel) -> Unit)? = null
    var headerListener: (() -> Unit)? = null
    var userIdListener: ((post: PostModel) -> Unit)? = null
    var placeIdListener: ((post: PostModel) -> Unit)? = null
    var linkPreviewListener: ((url: String) -> Unit)? = null

    override fun onCreateHeaderViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_post_header, parent, false)
        itemView.findViewById<TextView>(R.id.header_comment).let {
            it.setText("소모임을 만들어 보세요")
        }


        return HeaderViewHolder(itemView)
    }

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_post, parent, false)

        return ItemViewHolder(itemView)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        // TODO: profile image
        val viewHolder: HeaderViewHolder = holder as HeaderViewHolder
        viewHolder.view.setOnClickListener { headerListener?.invoke() }
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val post: PostModel = getItem(position)
        val viewHolder: ItemViewHolder = holder as ItemViewHolder
        val context: Context = viewHolder.contentView.context

        viewHolder.linkPreviewLoading.visibility = View.GONE
        viewHolder.linkPreview.visibility = View.GONE

        val linkPreviewCallback = object : LinkPreviewCallback {
            override fun onPre() {
                viewHolder.linkPreviewLoading.visibility = View.VISIBLE
            }

            override fun onPos(sourceContent: SourceContent, isNull: Boolean) {
                viewHolder.linkPreviewLoading.visibility = View.GONE
                if (isNull) return

                viewHolder.linkPreview.let {
                    it.visibility = View.VISIBLE
                    it.setOnClickListener { linkPreviewListener?.invoke(sourceContent.finalUrl) }
                    it.link_title.text = sourceContent.title
                    it.link_desc.text = sourceContent.description
                    try {
                        Glide.with(it.link_preview_image)
                                .load(sourceContent.images[0])
                                .into(it.link_preview_image)
                    } catch (e: IllegalArgumentException) {}
                }
            }
        }

        viewHolder.timeView.text = dateUtils.getTimeString(post.updatedAt, R.array.time_decor)
        viewHolder.cardView.setOnClickListener { itemListener?.invoke(post) }

        viewHolder.textCrawler.cancel()

        viewHolder.contentView.let {
            it.text = post.content
            val url = post.content.getWebUrl()

            if (url != null) {
                viewHolder.textCrawler.makePreview(linkPreviewCallback, url)
            }
        }
        viewHolder.titleView.let {
            it.text = context.getString(R.string.post_title, post.userName, post.placeName)
            it.movementMethod = LinkTouchMovementMethod
            val userClickableSpan = it.generateClickableSpan { userIdListener?.invoke(post) }
            val placeClickableSpan = it.generateClickableSpan { placeIdListener?.invoke(post) }
            post.userName.setSpan(it.text, userClickableSpan)
            post.placeName.setSpan(it.text, placeClickableSpan)
        }

        viewHolder.imageView.let {
            it.visibility = View.GONE
            val image = post.images.firstOrNull() ?: return
            Glide.with(it)
                    .load(image)
                    .into(object : SimpleTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable,
                                                     transition: Transition<in Drawable>?) {
                            it.visibility = View.VISIBLE
                            Glide.with(it)
                                    .load(resource)
                                    .into(it)
                        }
                    })
        }
    }

    data class Header(
            val image: String
    )

    private class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val view: View = itemView
        val profile: ImageView get() = itemView.profile_image
    }

    private class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView get() = itemView as CardView
        val contentView: TextView get() = itemView.content
        val linkPreview: View get() = itemView.link_preview
        val linkPreviewLoading: ContentLoadingProgressBar get() = itemView.link_preview_loading
        val titleView: TextView get() = itemView.title
        val timeView: TextView get() = itemView.time
        val imageView: ImageView get() = itemView.image
        val textCrawler = TextCrawler()
    }
}
