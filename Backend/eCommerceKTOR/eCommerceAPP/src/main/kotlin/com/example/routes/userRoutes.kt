package com.example.routes
import com.example.database.*
import com.example.model.AuthRequest
import com.example.model.AuthResponse
import com.example.model.User
import com.example.model.UserInfo
import com.example.security.hashing.HashingService
import com.example.security.hashing.SaltedHash
import com.example.security.token.TokenClaim
import com.example.security.token.TokenConfig
import com.example.security.token.TokenService
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import io.ktor.server.html.*
import kotlinx.html.*

fun Route.userRoutes(hashingService: HashingService, tokenService: TokenService, tokenConfig: TokenConfig, dao: UserImpl) {

    route("/users") {

        get("/validatePage/{email}") {
            val userEmail = call.parameters["email"]
            if (userEmail.isNullOrBlank()) return@get call.respondText(
                "[ERROR] No valid email has been entered.",
                status = HttpStatusCode.BadRequest
            )

            return@get call.respondHtml(status = HttpStatusCode.OK) {

                head {
                    title { +"Success Validation" }
                    style {
                        unsafe {
                            raw(
                                """
                       body {
                           font-family: 'Arial', sans-serif;
                           background-color: #f4f4f4;
                           text-align: center;
                           padding: 20px;
                       }
                       h1 {
                           color: #ff7900;
                       }
                       p {
                           font-size: 18px;
                           color: #333;
                       }
                       """
                            )
                        }
                    }
                }
                body {
                    h1 {
                        +"Thanks for verificate yourself"
                    }
                    p {
                        +"Now you can return to the app, to log in."
                    }

                    script {
                        unsafe {
                            raw(
                                """
                                function sendEmail() {
                                    var url = 'http://89.47.29.153:27031/users/validateEmail/$userEmail';
                                    fetch(url, {
                                        method: 'PATCH'
                                    })
                                }
                                """
                            )
                            +"sendEmail()"
                        }

                    }
                }
            }
        }

        patch("/validateEmail/{email}") {
            val userEmail = call.parameters["email"]
            if (userEmail.isNullOrBlank()) return@patch call.respondText(
                "[ERROR] No valid email has been entered.",
                status = HttpStatusCode.BadRequest
            )

            dao.updateUserValidation(userEmail, true)

            return@patch call.respondText("[SUCCESS] User have been validated", status = HttpStatusCode.Accepted)
        }

        get("/resetPasswordPage/{email}") {
            val userEmail = call.parameters["email"]
            if (userEmail.isNullOrBlank()) return@get call.respondText(
                "[ERROR] No valid email has been entered.",
                status = HttpStatusCode.BadRequest
            )

            return@get call.respondHtml(status = HttpStatusCode.OK) {


                head {
                    title("Password Reset")
                }
                body {
                    style {
                        unsafe {
                            raw(
                                """
                       body {
                           font-family: 'Arial', sans-serif;
                           background-color: #f4f4f4;
                           text-align: center;
                           padding: 20px;
                       }
                       h2 {
                           color: #ff7900;
                       }
                       p {
                           font-size: 18px;
                           color: #333;
                       }
                       button {
                           font-family: 'Arial', sans-serif;
                           background-color: #FF7900;
                           text-align: center;
                           padding: 16px;
                           font-size: 16px;
                           font-weight: bold;
                           color: white;
                           border-radius: 5px;
                           border-color: black;
                       }
                       """
                            )
                        }
                    }
                    script {
                        unsafe {
                            raw(
                                """
                                
                                function encryptAndSendPass() {
                                    document.addEventListener('DOMContentLoaded', function() {
                                        document.getElementById("submitButton").addEventListener("click", function(event) {
                                            event.preventDefault();
                                            function handleSubmit() {
                             
                                            const passwordField = document.getElementsByName('password')[0];
                                            const repeatPasswordField = document.getElementsByName('repeat_password')[0];
                                            const password = passwordField.value;
                                            const repeatPassword = repeatPasswordField.value;
                                            
                                            if (password !== repeatPassword) {
                                                // Display an error message or perform some other action
                                                console.error("Passwords don't match");
                                                return; // Prevent form submission if passwords don't match
                                            }
                            
                                            const encoder = new TextEncoder();
                                            const data = encoder.encode(password);
                            
                                            crypto.subtle.digest('SHA-256', data).then(encryptedData => {
                                                const encryptedPassword = Array.from(new Uint8Array(encryptedData)).map(byte => ('00' + byte.toString(16)).slice(-2)).join('');
                            
                                                const formData = new FormData();
                                                formData.append('password', encryptedPassword);
                            
                                                const url = '/users/resetPassword/$userEmail';
                            
                                                fetch(url, {
                                                    method: 'POST',
                                                    body: formData,
                                                })
                                                .then(response => {
                                                    // Handle the response from the server
                                                    if (response.ok) {
                                                        // Handle successful response (e.g., show success message)
                                                        console.log('Password reset successful')
                                                        console.log(encryptedPassword);
                                                        //window.location.href = '/users/resetPassword/$userEmail';
                                                        ;
                                                    } else {
                                                        // Handle errors from the server
                                                        console.error('Password reset failed');
                                                    }
                                                })
                                                .catch(error => {
                                                    // Handle fetch errors (e.g., network issues)
                                                    console.error('Error occurred:', error);
                                                });
                                            });
                                        };
                                       handleSubmit();
                                        });
                                    });
                                   }
                                """
                                +"encryptAndSendPass()"
                            )
                        }
                    }
                    h2 { +"Password Reset" }
                    form(action = "/users/resetPassword/$userEmail", method = FormMethod.post) {
                        p {
                            +"Password: "
                            passwordInput(name = "password")
                        }
                        p {
                            +"Repeat password: "
                            passwordInput(name = "repeat_password")
                        }
                        p {
                            button {
                                id = "submitButton"
                                +"Change password"
                            }
                        }
                    }
                }
            }
        }

        post("/resetPassword/{email}") {
            val userEmail = call.parameters["email"]
            val params = call.receiveParameters()
            val newPass = params["password"].toString()
            if (userEmail.isNullOrBlank()) return@post call.respondText(
                "[ERROR] No valid email has been entered.",
                status = HttpStatusCode.BadRequest
            )
            if (newPass.isNullOrBlank()) return@post call.respondText(
                "[ERROR] No valid password has been entered.",
                status = HttpStatusCode.BadRequest
            )
            val saltedHash = hashingService.generateSaltedHash(newPass)
            dao.updateUserPassword(userEmail, saltedHash.hash, saltedHash.salt)

            return@post call.respondHtml(status = HttpStatusCode.OK) {

                head {
                    title { +"Password change success" }
                    style {
                        unsafe {
                            raw(
                                """
                       body {
                           font-family: 'Arial', sans-serif;
                           background-color: #f4f4f4;
                           text-align: center;
                           padding: 20px;
                       }
                       h1 {
                           color: #ff7900;
                       }
                       p {
                           font-size: 18px;
                           color: #333;
                       }
                       """
                            )
                        }
                    }
                }
                body {
                    h1 {
                        +"Your password has been successfully changed"
                    }
                    p {
                        +"Now you can return to the app, to log in."
                    }
                }
            }
        }

        post("/login") {
            val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "The request is null.")
                return@post
            }

            // Get the user from the BD with the email provided in the request
            val user = dao.getUserByEmail(request.email)

            if (user == null) {
                call.respond(HttpStatusCode.NotFound, "Email not found.")
                return@post
            }
            // Verify the pass received with the pass and salt stored in the BD
            val isValidPass = hashingService.verify(
                request.password,
                    SaltedHash(user.userPass, user.userSalt)
            )

            if (!isValidPass) {
                call.respond(HttpStatusCode.Unauthorized, "Incorrect password")
                return@post
            }
            if (!user.userValidated) {
                call.respond(HttpStatusCode.Unauthorized, "User not validated")
                return@post
            }
            // Then create a token and return it
            val token = tokenService.generate(
                tokenConfig,
                TokenClaim("userEmail", user.userEmail)
            )
            call.respond(HttpStatusCode.OK, AuthResponse(token))
        }

        post("/signup") {
            val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val userExists = dao.getUserByEmail(request.email)
            if (userExists == null) {
                // Encrypts the password provided and creates a new user to store in the DB
                val saltedHash = hashingService.generateSaltedHash(request.password)
                val newUserInfo = UserInfo(
                    0,
                    "placeholder.png",
                    request.email,
                    false,
                    saltedHash.hash,
                    saltedHash.salt
                )

                val addedNewUser = dao.addUser(newUserInfo)

                if (addedNewUser) {
                    return@post call.respondText(
                        "[SUCCESS] New user created successfully.",
                        status = HttpStatusCode.Created
                    )
                } else {
                    return@post call.respondText(
                        "[ERROR] The new user could not be added.",
                        status = HttpStatusCode.BadRequest
                    )
                }
            } else {
                return@post call.respondText(
                    "[ERROR] There's an existing user with this email associated to.",
                    status = HttpStatusCode.NotAcceptable
                )
            }
        }

        get("/changeEmail/{oldmail}/{newmail}") {
            val oldMail = call.parameters["oldmail"]!!
            val newMail = call.parameters["newmail"]!!

            return@get call.respondHtml(status = HttpStatusCode.OK) {

                head {
                    title { +"Success Validation" }
                    style {
                        unsafe {
                            raw(
                                """
                   body {
                       font-family: 'Arial', sans-serif;
                       background-color: #f4f4f4;
                       text-align: center;
                       padding: 20px;
                   }
                   h1 {
                       color: #ff7900;
                   }
                   p {
                       font-size: 18px;
                       color: #333;
                   }
                   """
                            )
                        }
                    }
                }
                body {
                    h1 {
                        +"Thanks for verificate your new email, your email $oldMail has been changed to $newMail"
                    }
                    p {
                        +"Now you can return to the app, to log in."
                    }
                    script {
                        unsafe {
                            raw(
                                """
                            function sendEmail() {
                                var url = 'http://89.47.29.153:27031/users/changeEmail/$newMail';
                                fetch(url, {
                                    method: 'PATCH',
                                    body: '$oldMail'
                                })
                            }
                            """
                            )
                            +"sendEmail()"
                        }

                    }
                }
            }
        }

        patch("/changeEmail/{newEmail}") {
            val newEmail = call.parameters["newEmail"]
            val oldEmail = call.receiveText()

            if (newEmail.isNullOrBlank()) return@patch call.respondText(
                "[ERROR] No valid email has been entered.",
                status = HttpStatusCode.BadRequest
            )
            dao.updateUserEmail(oldEmail, newEmail)

            return@patch call.respondText("[SUCCESS] User email changed from $oldEmail to $newEmail", status = HttpStatusCode.Accepted)
        }

        authenticate {

            post("/verifyPassword") {
                call.principal<JWTPrincipal>()?.getClaim("userEmail", String::class)?.let { mail ->
                    val password = call.receiveText().replace("\"","")

                    dao.getUserByEmail(mail)?.let { user->

                        val isValidPass = hashingService.verify(
                            password,
                            SaltedHash(user.userPass, user.userSalt)
                        )
                        if (!isValidPass) {
                            return@post call.respond(HttpStatusCode.Unauthorized, true)
                        }
                        return@post call.respond(HttpStatusCode.OK, true)
                    }
                }
            }

            patch("/setNewPassword") {
                call.principal<JWTPrincipal>()?.getClaim("userEmail", String::class)?.let { mail ->
                    val request = call.receiveNullable<Map<String, String>>()?: kotlin.run{
                        call.respond(HttpStatusCode.BadRequest, "The request is null")
                        return@patch
                    }

                    dao.getUserByEmail(mail)?.let { user->
                        val currentPassword = request["CurrentPassword"]!!
                        val isValidPass = hashingService.verify(
                            currentPassword,
                            SaltedHash(user.userPass, user.userSalt)
                        )
                        if (!isValidPass) {
                            call.respond(HttpStatusCode.Unauthorized, "Incorrect password")
                            return@patch
                        }

                        val newPassword = request["NewPassword"]!!
                        val saltedHash = hashingService.generateSaltedHash(newPassword)
                        dao.updateUserPassword(mail, saltedHash.hash, saltedHash.salt)
                        call.respond(HttpStatusCode.OK, "Password updated")
                        return@patch
                    }
                }
                return@patch call.respond(HttpStatusCode.NotFound)
            }

            get("/loggedUser") {
                // hay que mandar Authorization header con Bearer token
                call.principal<JWTPrincipal>()?.getClaim("userEmail", String::class)?.let { mail ->
                    dao.getUserByEmail(mail)?.let { userInfo ->
                        val user = User(userInfo.userID, userInfo.userImage, userInfo.userEmail, userInfo.userValidated)
                        return@get call.respond(HttpStatusCode.OK, user)
                    }
                }
                return@get call.respond(HttpStatusCode.NotFound)
            }

            get {
                val allUsers = dao.getAllUsers()

                allUsers?.let {
                    call.respond(it)
                } ?: call.respondText("[ERROR] There are no users in the database.", status = HttpStatusCode.NotFound)
            }

            get("{id}/picture") {
                var userImage: File = File("")
                val userID = call.parameters["id"]
                if (userID.isNullOrBlank()) return@get call.respondText(
                    "[ERROR] No valid ID has been entered.",
                    status = HttpStatusCode.BadRequest
                )

                val userPhoto = dao.getUserById(userID.toInt())!!.userImage
                userImage = File("uploads/$userPhoto")

                if (userImage.exists()) {
                    call.respondFile(userImage)
                } else {
                    call.respondText("[ERROR] No image found.", status = HttpStatusCode.NotFound)
                }
            }

            delete("/{id?}") {
                if (call.parameters["id"].isNullOrBlank())
                    return@delete call.respondText(
                        "[ERROR] No valid ID has been entered.",
                        status = HttpStatusCode.BadRequest
                    )
                call.principal<JWTPrincipal>()?.getClaim("userEmail", String::class)?.let { mail ->
                    dao.getUserByEmail(mail)?.let { userInfo ->

                        val id = call.parameters["id"]!!.toInt()

                        if (userInfo.userID != id) {
                            return@delete call.respondText(
                                "[ERROR] You can't delete other users.",
                                status = HttpStatusCode.Unauthorized
                            )
                        }

                        val deleteUser = dao.deleteUser(id)

                        if (deleteUser) {
                            return@delete call.respondText(
                                "[SUCCESS] User successfully deleted.",
                                status = HttpStatusCode.Created
                            )
                        } else {
                            return@delete call.respondText(
                                "[ERROR] The user could not be deleted.",
                                status = HttpStatusCode.BadRequest
                            )
                        }
                    }
                }
            }

            put("/edit") {
                // Update user info in db where mail = (mail from token)
                val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
                    return@put call.respond(HttpStatusCode.BadRequest)
                }
                call.principal<JWTPrincipal>()?.getClaim("userEmail", String::class)?.let { mail ->
                    dao.getUserByEmail(mail)?.let { user ->
                        val saltedHash = hashingService.generateSaltedHash(request.password)
                        val updatedUserInfo = UserInfo(
                            user.userID,
                            user.userImage,
                            request.email,
                            user.userValidated,
                            saltedHash.hash,
                            saltedHash.salt
                        )
                        val updateUser = dao.updateUserInfo(updatedUserInfo)
                        return@put call.respond(HttpStatusCode.OK, updateUser)
                    }
                }
            }

            put("/picture") {
                // Update user picture in db where mail = (mail from token)
                call.principal<JWTPrincipal>()?.getClaim("userEmail", String::class)?.let { mail ->
                    val user = dao.getUserByEmail(mail)!!
                    val oldImage = user.userImage
                    val userID = user.userID
                    val data = call.receiveMultipart()
                    var userImage = ""

                    data.forEachPart { part ->
                        when (part) {
                            is PartData.FormItem -> Unit
                            is PartData.FileItem -> {

                                userImage =
                                    part.originalFileName as String // A user.image le asignamos en formato string la ruta donde se guardará la imagen

                                val fileBytes =
                                    part.streamProvider().readBytes() //LEEMOS LA IMAGEN QUE HA PASADO POR EL POST
                                File("uploads/$userImage").writeBytes(fileBytes)//GUARDA LA IMAGEN QUE HA PASADO POR EL POST A LA CARPETA "uploads"
                                //EN BASES DE DATOS SOLO GUARDAR URL del archivo
                            }

                            else -> {
                                Unit
                            }
                        }
                    }

                    val updatePicture = dao.updateUserPicture(userImage, userID)

                    if (updatePicture) {
                        // Checks if there's an old picture of the user in the uploads directory and deletes it if it exists.
                        var doneDelete = false
                        val oldFilePath = File("uploads/$oldImage")
                        if (oldFilePath.exists()) {
                            doneDelete = oldFilePath.delete()
                        }
                        return@put call.respondText(
                            "[SUCCESS] Picture of user $userID successfully modified. Old picture deleted: $doneDelete",
                            status = HttpStatusCode.OK
                        )
                    } else {
                        return@put call.respondText(
                            "[ERROR] The picture could not be modified.",
                            status = HttpStatusCode.BadRequest
                        )
                    }
                }
            }
        }
    }
}


