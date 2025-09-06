package com.example.lab07

enum class NotificationType {
    GENERAL,
    NEW_MEETING
}

data class Notification(
    val id: Int,
    val title: String,
    val body: String,
    val sendAt: String,
    val type: NotificationType
)

fun generateFakeNotifications(): List<Notification> {
    val notifications = mutableListOf<Notification>()
    val titles = listOf(
        "Nueva versión disponible",
        "Nueva reunión agendada con Koalit",
        "Mensaje de Maria",
        "Capacitación: jetpack compose internals"
    )
    val bodies = listOf(
        "La aplicación ha sido actualizada a v1.0.2. Ve a la PlayStore y actualízala!",
        "Koalit te ha enviado un evento para que agregues a tu calendario",
        "No te olvides de asistir a esta capacitación mañana, a las 6pm, en el Intecap.",
        "Inicio de la capacitación 'Jetpack Compose Internals', no faltes"
    )
    val types = NotificationType.entries.toTypedArray()

    for (i in 1..50) {
        val daysAgo = (0..10).random()
        val hoursAgo = (0..23).random()
        val minutesAgo = (0..59).random()
        val sendAt = "30 Sept 10:45am"
        notifications.add(
            Notification(
                id = i,
                title = titles.random(),
                body = bodies.random(),
                sendAt = sendAt,
                type = types.random()
            )
        )
    }
    return notifications
}