package com.example.ecommercemobile.ui.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ecommercemobile.databinding.FragmentEditProfileBinding
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.io.File

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    lateinit var userViewModel: UserViewModel
    var imagePath = ""
    var imageUri: Uri? = null
    private val REQUEST_PERMISSIONS = 1
    var newImageName = ""
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                runBlocking {
                    imageUri = data.data!!
                    binding.profileIV.setImageURI(imageUri)
                    // Get the file name from the URI
                    newImageName = getFileName(imageUri!!)

                }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater,container,false)
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        binding.topAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileIV.setImageBitmap(userViewModel.userImage.value)

        binding.photoBT.setOnClickListener{
            if(ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 10)
            }
            selectImage()
        }
        //LOGIN TO VALIDATE CREDENTIALS -> EDIT USER -> LOGIN AGAIN FOR PREFS
         val emailET = binding.emailET
        val newPassET = binding.passwordET
        val confirmNewPassET = binding.repeatPassET

        binding.editProfileBT.setOnClickListener {
            val newEmail = binding.emailET.editText?.text.toString()
            val newPass = binding.passwordET.editText?.text.toString()
            val confirmNewPass = binding.repeatPassET.editText?.text.toString()
            val encryptedPass: String

            binding.editProfileBT.startAnimation()

            if (userViewModel.validateEmail(emailET, newEmail) && userViewModel.validatePassword(newPassET, newPass)
                && (userViewModel.confirmPassword(confirmNewPassET, newPass, confirmNewPass))) {

                // Update the user with the new email and password
                encryptedPass = userViewModel.encryptPassword(newPass)
                userViewModel.putUser(newEmail, encryptedPass)

                // Check if the image didn't change or did change
                if (imageUri != null){
                    try {
                        imagePath = getPathFromUri(imageUri!!)!!
                    } catch (e: java.lang.NullPointerException){
                        Log.e("Error ", e.message, e.cause)
                    }
                    val imageFile = File(imagePath)
                    // Update user image
                    userViewModel.putUserPicture(imageFile)
                }
            } else {
                Toast.makeText(requireContext(), "Invalid credentials", Toast.LENGTH_SHORT).show()
                binding.editProfileBT.revertAnimation()
            }

            // When the user is updated, navigate to ProfileFragment
            userViewModel.user.observe(viewLifecycleOwner){
                binding.editProfileBT.revertAnimation()
                val toProfile = EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment()
                findNavController().navigate(toProfile)
            }
        }
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context?.contentResolver?.query(uri,
                null, null, null, null)
            cursor?.let {
                if (it.moveToFirst()) {
                    val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                    result = cursor.getString(column_index)
                }
                cursor.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result!!
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }

    private fun getPathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireActivity().contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val imagePath = cursor.getString(columnIndex)
        cursor.close()
        return imagePath
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    selectImage()
                } else {
                    Toast.makeText(requireContext(),
                        "Please accept the permissions.", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

}