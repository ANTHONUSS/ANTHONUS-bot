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

    private const val CREDITS_TITLE = "Credits du bot"
    private val CREDITS_VALUE = """
        Developpe par ANTHONUS, (je pense que c'est assez evident)
        [Mon site web](https://anthonus.fr)
    """.trimIndent()

    fun createEmbed(
        title: String? = null,
        description: String? = null,
        color: Color? = null,
        thumbnailUrl: String? = null,
        imageUrl: String? = null,
        footerText: String? = null,
        footerIconUrl: String? = null,
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
        if (footerText != null) embed.setFooter(footerText, footerIconUrl)
        if (authorName != null) embed.setAuthor(authorName, authorUrl, authorIconUrl)
        if (timestamp != null) embed.setTimestamp(timestamp)

        fields.forEach { field ->
            embed.addField(field.name, field.value, field.inline)
        }

        embed.addField(CREDITS_TITLE, CREDITS_VALUE, false)

        return embed.build()
    }
}