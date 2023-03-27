export interface IFile {
  id?: number;
  fileContentType?: string | null;
  file?: string | null;
}

export class File implements IFile {
  constructor(public id?: number, public fileContentType?: string | null, public file?: string | null) {}
}

export function getFileIdentifier(file: IFile): number | undefined {
  return file.id;
}
