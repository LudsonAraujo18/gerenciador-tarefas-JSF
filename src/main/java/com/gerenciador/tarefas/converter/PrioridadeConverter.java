package com.gerenciador.tarefas.converter;

import com.gerenciador.tarefas.model.Prioridade;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "prioridadeConverter", forClass = Prioridade.class)
public class PrioridadeConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Prioridade.valueOf(value); // Converte String para Enum
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        return ((Prioridade) value).name(); // Converte Enum para String
    }
}
