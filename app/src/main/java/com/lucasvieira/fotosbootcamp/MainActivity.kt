@file:Suppress("DEPRECATION")

package com.lucasvieira.fotosbootcamp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        private val PERMISSION_CODE_IMAGE_PICK = 1000
        private val IMAGE_PICK_CODE = 1001
        private val PERMISSION_CODE_CAMERA_CAPTURE = 2000

        //variaveis de acesso nativos
        private val ACESSO_NEGADO = PackageManager.PERMISSION_DENIED
        private val ACESSO_GARANTIDO = PackageManager.PERMISSION_GRANTED
        private val LEITURA = Manifest.permission.READ_EXTERNAL_STORAGE
        private val ESCRITA = Manifest.permission.WRITE_EXTERNAL_STORAGE
        private val CAMERA = Manifest.permission.CAMERA

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // evento do botão para buscar imagem da galeria
        pick_button.setOnClickListener{
            //caso a versão do celular seja inferior ao Marshmallow, pedir permissão de acesso
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(LEITURA) == (ACESSO_NEGADO)){
                    val permission = arrayOf(LEITURA)
                    requestPermissions(permission, PERMISSION_CODE_IMAGE_PICK)
                }
                else {
                    pickImageFromGallery()
                }
            }
            else {
                pickImageFromGallery()
            }
        }

        // evento do botão para abrir a camera
        open_camera_button.setOnClickListener{
            //caso a versão do celular seja inferior ao Marshmallow, pedir permissão de acesso
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(CAMERA) == (ACESSO_NEGADO)
                    || checkSelfPermission(ESCRITA) == (ACESSO_NEGADO)){
                    val permissions = arrayOf(CAMERA, ESCRITA)
                    requestPermissions(permissions, PERMISSION_CODE_CAMERA_CAPTURE)
                } else {
                    openCamera()
                }
            }
            else {
                openCamera()
            }
        }
    }

    //função para abrir a galeria de foto
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

//    função para abrir a camera do celular
    private fun openCamera() {
        TODO("Implementar a função para abrir camera")
    }


    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
      // verifica o codigo de requisição e direciona para a função necessária
        when(requestCode){
            //permissão para buscar imagem
            PERMISSION_CODE_IMAGE_PICK -> {
                if (grantResults.size > 0 && grantResults[0] == ACESSO_GARANTIDO) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
                }
            }

            // requisição para tirar foto
            PERMISSION_CODE_CAMERA_CAPTURE -> {
                if (grantResults.size > 1 &&
                        grantResults[0] == ACESSO_GARANTIDO &&
                        grantResults[1] == ACESSO_GARANTIDO) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //após trocar de tela para tirar ou buscar uma foto, carregar a imagem da tela principal
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            image_view.setImageURI(data?.data)
        }
    }
}