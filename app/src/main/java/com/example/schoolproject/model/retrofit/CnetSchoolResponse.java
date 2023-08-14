package com.example.schoolproject.model.retrofit;

import java.util.List;

public class CnetSchoolResponse {
    private DataSearch dataSearch;

    public DataSearch getDataSearch(){
        return dataSearch;
    }
    public void setDataSearch(DataSearch dataSearch){
        this.dataSearch = dataSearch;
    }

    public class DataSearch{
        private List<CnetSchool> content;
        public List<CnetSchool> getContent(){
            return content;
        }
        public void setContent(List<CnetSchool> content){
            this.content = content;
        }
    }
}
