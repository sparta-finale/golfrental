package com.golfRental.domain.category.exception;

import com.golfRental.common.exception.GlobalException;

public class CategoryException extends GlobalException {
    public CategoryException(CategoryErrorCode categoryErrorCode) {
        super(categoryErrorCode);
    }
}
