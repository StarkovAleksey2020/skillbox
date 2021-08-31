package com.example.MyBookShopApp.entity.enumiration;

/**
 * Project name: MyBookShopApp
 * Date: 8/9/2021
 * Author: dishmitov
 * Description:
 * Типы файлов книг:
 * PDF
 * EPUB
 * FB2
 */

public enum BookFileTypeEnum {
    PDF(".pdf"),
    EPUB(".epub"),
    FB2(".fb2");

    private final String fileExtensionString;

    BookFileTypeEnum(String fileExtensionString) {
        this.fileExtensionString = fileExtensionString;
    }

    public static String getExtensionStringByTypeId(Integer typeId) {
        switch (typeId) {
            case 1:
                return BookFileTypeEnum.PDF.fileExtensionString;
            case 2:
                return BookFileTypeEnum.EPUB.fileExtensionString;
            case 3:
                return BookFileTypeEnum.FB2.fileExtensionString;
            default: return "";
        }
    }
}