package com.uwefuchs.demo.kotlin.myjanitor.web

import com.uwefuchs.demo.kotlin.pocket.api.Item
import com.uwefuchs.demo.kotlin.pocket.api.Pocket
import com.uwefuchs.demo.kotlin.pocket.api.PocketException
import com.uwefuchs.demo.kotlin.pocket.api.Sort
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class ItemController(@Autowired private val pocket: Pocket)  {
    @GetMapping("/")
    fun home() = "redirect:/items";

    @GetMapping("/items")
    fun overview(model: Model): String {
        val items = pocket.retrieveOperations().items(sort = Sort.NEWEST);

        with(model) {
            addAttribute("items", items);
            addAttribute("count", items.size);
        }

        return "items/overview";
    }

    @GetMapping("/items/delete")
    fun delete(@RequestParam(required = true) id: String, model: Model): String {
        val success = pocket.modifyOperations().delete(id);

        if (!success) {
            throw PocketException("Item could not be deleted!");
        }

        with(model) {
            addAttribute("success", true);
            addAttribute("action", "deleted");
        }

        return "redirect:/items";
    }

    @GetMapping("/items/archive")
    fun archive(@RequestParam(required = true) id: String, model: Model): String {
        val success = pocket.modifyOperations().archive(id);

        if (!success) {
            throw PocketException("Item could not be archived!");
        }

        with(model) {
            addAttribute("success", true);
            addAttribute("action", "archived");
        }

        return "redirect:/items"
    }


    @ExceptionHandler
    fun error(model: Model, cause: Exception): String {
        model.addAttribute("cause", cause)

        return "error"
    }
}