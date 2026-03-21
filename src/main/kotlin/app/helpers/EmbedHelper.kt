package app.helpers

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import java.awt.Color
import java.time.Instant

object EmbedHelper {
    data class Field(
        val name: String,
        val value: String,
        val inline: Boolean = false
    )

    private const val CREDITS_TITLE = "Bot développé par ANTHONUS -> /info"
    private const val CREDITS_URL = "https://avatar-cyan.vercel.app/api/pfp/722086214949404682/smallimage"

    enum class Type(val color: Color, val emoji: String) {
        SUCCESS(Color.GREEN, ":white_check_mark:"),
        WARNING(Color.YELLOW, ":warning:"),
        ERROR(Color.RED, ":x:"),
        INFO(Color.BLUE, ":information_source:")
    }

    fun createEmbed(
        type: Type,
        title: String,
        description: String? = null,
        thumbnailUrl: String? = null,
        imageUrl: String? = null,
        authorName: String? = null,
        authorUrl: String? = null,
        authorIconUrl: String? = null,
        timestamp: Instant? = null,
        fields: List<Field> = emptyList()
    ): MessageEmbed {
        val themedTitle = "${type.emoji} $title ${type.emoji}"

        return createEmbed(
            title = themedTitle,
            color = type.color,
            description = description,
            thumbnailUrl = thumbnailUrl,
            imageUrl = imageUrl,
            authorName = authorName,
            authorUrl = authorUrl,
            authorIconUrl = authorIconUrl,
            timestamp = timestamp,
            fields = fields
        )
    }

    fun createEmbed(
        title: String? = null,
        description: String? = null,
        color: Color? = null,
        thumbnailUrl: String? = null,
        imageUrl: String? = null,
        authorName: String? = null,
        authorUrl: String? = null,
        authorIconUrl: String? = null,
        timestamp: Instant? = null,
        fields: List<Field> = emptyList()
    ): MessageEmbed {
        val embed = EmbedBuilder()

        if (title != null) embed.setTitle(title)
        if (description != null) embed.setDescription(description)
        if (color != null) embed.setColor(color)
        if (thumbnailUrl != null) embed.setThumbnail(thumbnailUrl)
        if (imageUrl != null) embed.setImage(imageUrl)
        if (authorName != null) embed.setAuthor(authorName, authorUrl, authorIconUrl)
        if (timestamp != null) embed.setTimestamp(timestamp)

        fields.forEach { field ->
            embed.addField(field.name, field.value, field.inline)
        }

        embed.setFooter(CREDITS_TITLE, CREDITS_URL)

        return embed.build()
    }
}