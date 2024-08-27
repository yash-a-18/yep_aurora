package com.axiom.patienttracker.domain.data

final case class User(
    id: Long,
    email: String,
    hashedPassword: String
):
    def toUserID =
        UserID(id, email)

final case class  UserID(
    id: Long,
    email: String
)
