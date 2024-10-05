package com.example.myshoppinglistapp



import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingItem(
    val id:Int,
    var name:String,
    var quantity:Int,
    var isEditting:Boolean = false
)


@Composable
fun ShoppingListApp() {
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center) {
        Button(onClick = {showDialog= true},modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("add Item")
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            items(sItems) {

                item ->
                if (item.isEditting) {
                    ShoppingItemEditor (item = item,onEditComplete = {
                        editedName,editedQuant->
                        sItems = sItems.map{it.copy(isEditting = false)}
                        val editedItem = sItems.find{it.id == item.id }
                        editedItem?.let {
                            it.name = editedName
                            it.quantity = editedQuant
                        }
                    })
                } else {
                    ShoppingListItem(item = item, onEditClick = {
                        // finding out which item are being editted and changing it to true
                        sItems = sItems.map {it.copy(isEditting = it.id == item.id)}

                    }, onDeleteClick = {
                        sItems = sItems-item
                    })
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(onDismissRequest = {showDialog = false},
            confirmButton = {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = {
                        if (itemName.isNotBlank()) {
                            val newItem = ShoppingItem(
                                id = sItems.size+1,
                                name = itemName,
                                quantity = itemQuantity.toInt()


                            )
                            sItems = sItems +newItem
                            showDialog = false
                            itemName = ""
                            itemQuantity = ""

                        }
                    }) {
                        Text("Add")
                    }

                    Button(onClick = {showDialog = false}) {
                        Text("Cancel")
                    }
                }


            },
            title = {Text("Add shopping Item")},
            text = {
                Column {
                    OutlinedTextField(value = itemName,
                        onValueChange = {itemName = it},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    )




                    OutlinedTextField(value = itemQuantity,
                        onValueChange = {itemQuantity = it},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    )








                }
            })
//        AlertDialog(onDismissRequest = {showDialog = false}) {
//            Text("I am an alert Dailog")
//        }
    }




}


@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick:()->Unit,
    onDeleteClick:()->Unit

) {
    Row(
        modifier = Modifier.padding(8.dp).fillMaxWidth().border(
           border = BorderStroke(2.dp,Color(0XFF018786)),
            shape = RoundedCornerShape(20)
        )
        )  {
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text ="Qty: ${item.quantity}",modifier = Modifier.padding(8.dp))
        Row (modifier = Modifier.padding(16.dp)) {

            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }

            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }


        }


    }


}

@Composable
fun ShoppingItemEditor(
    item: ShoppingItem,
    onEditComplete:(String,Int)->Unit
) {
    var editName by remember { mutableStateOf(item.name) }
    var editQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditting by remember { mutableStateOf(item.isEditting) }

    Row(modifier = Modifier.fillMaxWidth().background(Color.White).padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly) {
        Column {
            BasicTextField(
                value = editName,
                onValueChange = {editName = it},
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            ) {

            }


            BasicTextField(
                value = editQuantity,
                onValueChange = {editQuantity = it},
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            ) {

            }
        }
        Button(onClick = {
            isEditting = false
            onEditComplete(editName,editQuantity.toIntOrNull()?:1)
        }) {
            Text("save" +
                    "")
        }

    }
}


