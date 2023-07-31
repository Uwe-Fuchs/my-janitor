package com.uwefuchs.demo.kotlin.myjanitor

import com.uwefuchs.demo.kotlin.pocket.api.Pocket
import com.uwefuchs.demo.kotlin.pocket.api.Sort
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ItemController(@Autowired private val pocket: Pocket)  {
    @GetMapping("/")
    fun home() = "redirect:/items"

    @GetMapping("/items")
    fun overview(model: Model): String {
        val items = pocket.retrieveOperations().items(sort = Sort.NEWEST)

        with(model) {
            addAttribute("items", items)
            addAttribute("count", items.size)
        }

        return "items/overview"
    }

    @ExceptionHandler
    fun error(model: Model, cause: Exception): String {
        model.addAttribute("cause", cause)

        return "error"
    }
}