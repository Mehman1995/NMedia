package ru.netology.nmedia.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dto.Post

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {

    companion object {
        val DDL_POSTS = """
        CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
            ${PostColumns.COLUMN_LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_LIKES_COUNT} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARE} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARE_COUNT} INTEGER NOT NULL DEFAULT 0
        );
        """.trimIndent()

        val DDL_DRAFT = """
            CREATE TABLE ${DraftColumns.TABLE} (
            ${DraftColumns.COLUMN_CONTENT} TEXT
            );
        """.trimIndent()
    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            if (post.id != 0L) {
                put(PostColumns.COLUMN_ID, post.id)
            }
            put(PostColumns.COLUMN_AUTHOR, post.author)
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_PUBLISHED, post.published)
            put(PostColumns.COLUMN_LIKED_BY_ME, post.likedByMe)
            put(PostColumns.COLUMN_LIKES_COUNT, post.likesCount)
            put(PostColumns.COLUMN_SHARE, post.share)
            put(PostColumns.COLUMN_SHARE_COUNT, post.sharesCount)
        }

        val id = db.replace(PostColumns.TABLE, null, values)
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun likedById(id: Long) {
        db.execSQL(
            """
                UPDATE posts SET 
                likesCount = likesCount + CASE WHEN likedByMe THEN -1 ELSE 1 END,
                likedByMe = CASE WHEN likedByMe THEN 0 else 1 END
                WHERE id = ?;
            """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
                UPDATE posts SET 
                shareCount = shareCount + 1,
                share = 1
                WHERE id = ?;
                
            """.trimIndent(), arrayOf(id)
        )
    }

    override fun saveDraft(draft: String?) {
        if (draft == null) {
            db.delete(DraftColumns.TABLE, null, null)
        } else {
            val values = ContentValues().apply {
                    put(PostColumns.COLUMN_CONTENT, draft)
                }
            db.replace(DraftColumns.TABLE, null, values)
        }
    }

    override fun getDraft(): String? =
        db.query(
            DraftColumns.TABLE,
            arrayOf(DraftColumns.COLUMN_CONTENT),
            null,
            null,
            null,
            null,
            null
        ).use {
            if (!it.moveToFirst()) {
                null
            } else {
                it.getString(it.getColumnIndexOrThrow(DraftColumns.COLUMN_CONTENT))
            }
        }


    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
                likedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                likesCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES_COUNT))
            )
        }
    }
}

object PostColumns {
    const val TABLE = "posts"
    const val COLUMN_ID = "id"
    const val COLUMN_AUTHOR = "author"
    const val COLUMN_CONTENT = "content"
    const val COLUMN_PUBLISHED = "published"
    const val COLUMN_LIKED_BY_ME = "likedByMe"
    const val COLUMN_LIKES_COUNT = "likesCount"
    const val COLUMN_SHARE = "share"
    const val COLUMN_SHARE_COUNT = "shareCount"

    val ALL_COLUMNS = arrayOf(
        COLUMN_ID,
        COLUMN_AUTHOR,
        COLUMN_CONTENT,
        COLUMN_PUBLISHED,
        COLUMN_LIKED_BY_ME,
        COLUMN_LIKES_COUNT,
        COLUMN_SHARE,
        COLUMN_SHARE_COUNT
    )

}

object DraftColumns {
    const val TABLE = "draft"
    const val COLUMN_CONTENT = "content"
}