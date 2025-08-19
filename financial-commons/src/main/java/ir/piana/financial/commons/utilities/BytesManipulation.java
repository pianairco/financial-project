package ir.piana.financial.commons.utilities;

public class BytesManipulation {
    public static byte[] leftPadBytes(byte[] input, byte padByte, int length) {
        if (input == null) {
            input = new byte[0];
        }
        if (input.length >= length) {
            return input;
        }
        byte[] padded = new byte[length];
        int padLength = length - input.length;
        // Fill padding bytes at the start
        for (int i = 0; i < padLength; i++) {
            padded[i] = padByte;
        }
        // Copy original bytes after padding
        System.arraycopy(input, 0, padded, padLength, input.length);
        return padded;
    }

    public static byte[] rightPadBytes(byte[] input, byte padByte, int length) {
        if (input == null) {
            input = new byte[0];
        }
        if (input.length >= length) {
            return input;
        }
        byte[] padded = new byte[length];
        // Copy original bytes at the start
        System.arraycopy(input, 0, padded, 0, input.length);
        // Fill padding bytes after original bytes
        for (int i = input.length; i < length; i++) {
            padded[i] = padByte;
        }
        return padded;
    }

    public static byte[] appendByteAtFirst(byte[] originalArray, byte byteToAdd) {
        if (originalArray == null) {
            return new byte[]{byteToAdd};
        }

        byte[] newArray = new byte[originalArray.length + 1];
        newArray[0] = byteToAdd;
        System.arraycopy(originalArray, 0, newArray, 1, originalArray.length);
        return newArray;
    }

    public static byte[] appendBytes(byte[] firstPart, byte[] secondParts) {
        if ((firstPart == null || firstPart.length == 0) &&
                (secondParts == null || secondParts.length == 0)) {
            return new byte[0];
        } else if (firstPart == null) {
            firstPart = new byte[]{0};
        } else if (secondParts == null) {
            secondParts = new byte[]{0};
        }
        byte[] newArray = new byte[firstPart.length + secondParts.length];
        if (firstPart.length > 0) {
            System.arraycopy(firstPart, 0, newArray, 0, firstPart.length);
        }
        if (secondParts.length > 0) {
            System.arraycopy(secondParts, 0, newArray, firstPart.length, secondParts.length);
        }
        return newArray;
    }

    public static byte[] appendByteAtEnd(byte[] originalArray, byte byteToAdd) {
        if (originalArray == null) {
            return new byte[]{byteToAdd};
        }

        byte[] newArray = new byte[originalArray.length + 1];
        System.arraycopy(originalArray, 0, newArray, 0, originalArray.length);
        newArray[newArray.length - 1] = byteToAdd;
        return newArray;
    }

    public static byte[] getSubArray(byte[] originalArray, int startIndex, int length) {
        if (originalArray == null || length <= 0) {
            return new byte[0]; // Return empty array for invalid input
        }

        // Basic bounds checking for robustness
        if (startIndex < 0 || startIndex >= originalArray.length) {
            // Handle error: startIndex out of bounds
            // For simplicity, returning an empty array, but you might throw an exception
            System.err.println("Error: startIndex is out of bounds.");
            return new byte[0];
        }

        // Adjust length if it goes beyond the original array's bounds
        if (startIndex + length > originalArray.length) {
            length = originalArray.length - startIndex;
        }

        byte[] subArray = new byte[length];
        System.arraycopy(originalArray, startIndex, subArray, 0, length);
        return subArray;
    }

    public static byte[] getSubArray(byte[] originalArray, int startIndex) {
        if (originalArray == null || startIndex <= 0) {
            return new byte[0]; // Return empty array for invalid input
        }
        return getSubArray(originalArray, startIndex, originalArray.length - startIndex);
    }

    public static byte[] changePartsAsNewBytes(byte[] originalArray, int firstIndex, int secondIndex, int length) {
        byte[] bytes = new byte[originalArray.length];
        System.arraycopy(originalArray, 0, bytes, 0, firstIndex);
        System.arraycopy(originalArray, secondIndex, bytes, firstIndex, length);
        System.arraycopy(originalArray, firstIndex, bytes, firstIndex + length, length);
        System.arraycopy(originalArray, secondIndex + length, bytes, secondIndex + length, originalArray.length - secondIndex - length);
        return bytes;
    }

    public static byte[] padToBlockSize(byte[] data, int blockSize) {
        int paddingLength = blockSize - (data.length % blockSize);
        if (paddingLength == blockSize) return data; // Already aligned
        byte[] padded = new byte[data.length + paddingLength];
        System.arraycopy(data, 0, padded, 0, data.length);
        return padded;
    }
}
